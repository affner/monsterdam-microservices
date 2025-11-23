import { IHelpRelatedArticle } from 'app/shared/model/help-related-article.model';
import { IHelpSubcategory } from 'app/shared/model/help-subcategory.model';

export interface IHelpQuestion {
  id?: number;
  title?: string;
  content?: string;
  isDeleted?: boolean;
  questions?: IHelpRelatedArticle[] | null;
  subcategory?: IHelpSubcategory | null;
}

export const defaultValue: Readonly<IHelpQuestion> = {
  isDeleted: false,
};
