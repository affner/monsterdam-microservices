import { IPostPoll } from 'app/shared/model/post-poll.model';
import { IPollVote } from 'app/shared/model/poll-vote.model';

export interface IPollOption {
  id?: number;
  optionDescription?: string;
  voteCount?: number;
  poll?: IPostPoll;
  votes?: IPollVote[] | null;
}

export const defaultValue: Readonly<IPollOption> = {};
