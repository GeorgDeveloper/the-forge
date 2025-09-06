import { EmployeesCountRange } from 'app/entities/enumerations/employees-count-range.model';
import { ITeam } from 'app/entities/team/team.model';

export interface IOrganization {
  id: number;
  fullName?: string | null;
  shortName?: string | null;
  activityAreas?: string | null;
  tagline?: string | null;
  legalAddress?: string | null;
  actualAddress?: string | null;
  inn?: string | null;
  kpp?: string | null;
  ogrn?: string | null;
  okpo?: string | null;
  bankName?: string | null;
  bankBik?: string | null;
  bankCorrAccount?: string | null;
  bankSettlementAccount?: string | null;
  phoneMain?: string | null;
  phoneSales?: string | null;
  phoneSupport?: string | null;
  emailMain?: string | null;
  emailPartners?: string | null;
  emailSupport?: string | null;
  website?: string | null;
  foundedYear?: number | null;
  employeesCountRange?: keyof typeof EmployeesCountRange | null;
  keyPersons?: string | null;
  products?: string | null;
  partners?: string | null;
  teams?: ITeam[] | null;
}

export type NewOrganization = Omit<IOrganization, 'id'> & { id: null };
