export interface ITeam {
  id: number;
  teamName?: string | null;
}

export type NewTeam = Omit<ITeam, 'id'> & { id: null };
