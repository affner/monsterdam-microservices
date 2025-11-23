import { IHelpSubcategory } from 'app/shared/model/help-subcategory.model';

export interface IHelpCategory {
  id?: number;
  name?: string;
  isDeleted?: boolean;
  subCategories?: IHelpSubcategory[] | null;
}

export const defaultValue: Readonly<IHelpCategory> = {
  isDeleted: false,
};
