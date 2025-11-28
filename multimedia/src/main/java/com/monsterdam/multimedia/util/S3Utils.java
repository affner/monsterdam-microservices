package com.monsterdam.multimedia.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class S3Utils {

    private final Logger log = LoggerFactory.getLogger(S3Utils.class);
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    public S3Utils(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String saveFile(byte[] archivo) {
        String archivoRef = UUID.randomUUID().toString(); // Genera un identificador único
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(archivo.length);

            // Convertir byte[] a InputStream
            InputStream inputStream = new ByteArrayInputStream(archivo);

            amazonS3.putObject(bucketName, archivoRef, inputStream, metadata);
        } catch (Exception e) {
            log.error("Error al subir el archivo a MinIO: {}", e.getMessage());
            throw new RuntimeException("Error al subir el archivo a MinIO", e);
        }
        return archivoRef;
    }

    public byte[] getFile(String s3Key) {
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, s3Key);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        } catch (AmazonS3Exception e) {
            if (e.getErrorCode().equals("NoSuchKey")) {
                log.warn("Archivo no encontrado en MinIO: {}", e.getMessage());
                return null; // o manejar de otra manera que prefieras
            }
            throw e; // Si es otro tipo de error, lo lanzamos para manejarlo en otro lugar
        } catch (IOException e) {
            log.error("Error al leer el archivo de S3: {}", e.getMessage());
            throw new RuntimeException("Error al leer el archivo de S3", e);
        }
    }
    // Puedes agregar más métodos aquí, como eliminarArchivo, actualizarArchivo, etc.
}
