import { IHelpQuestion } from 'app/shared/model/help-question.model';

export interface IHelpRelatedArticle {
  id?: number;
  title?: string;
  content?: string;
  relatedArticles?: IHelpQuestion[] | null;
}

export const defaultValue: Readonly<IHelpRelatedArticle> = {};
