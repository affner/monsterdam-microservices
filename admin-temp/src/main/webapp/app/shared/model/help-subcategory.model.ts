import { IHelpQuestion } from 'app/shared/model/help-question.model';
import { IHelpCategory } from 'app/shared/model/help-category.model';

export interface IHelpSubcategory {
  id?: number;
  name?: string;
  isDeleted?: boolean;
  questions?: IHelpQuestion[] | null;
  category?: IHelpCategory | null;
}

export const defaultValue: Readonly<IHelpSubcategory> = {
  isDeleted: false,
};
