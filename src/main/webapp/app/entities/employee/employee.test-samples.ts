import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 8899,
  firstName: 'Зоя',
  lastName: 'Зыков',
  birthDate: dayjs('2025-03-20'),
  employeeNumber: 'without past',
  hireDate: dayjs('2025-03-20'),
};

export const sampleWithPartialData: IEmployee = {
  id: 15019,
  firstName: 'Венедикт',
  lastName: 'Лапина',
  birthDate: dayjs('2025-03-19'),
  employeeNumber: 'mysteriously as sway',
  hireDate: dayjs('2025-03-20'),
};

export const sampleWithFullData: IEmployee = {
  id: 19019,
  firstName: 'Самуил',
  lastName: 'Савельев',
  birthDate: dayjs('2025-03-20'),
  employeeNumber: 'ah annex astride',
  hireDate: dayjs('2025-03-20'),
};

export const sampleWithNewData: NewEmployee = {
  firstName: 'Вадим',
  lastName: 'Власов',
  birthDate: dayjs('2025-03-19'),
  employeeNumber: 'alongside',
  hireDate: dayjs('2025-03-20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
