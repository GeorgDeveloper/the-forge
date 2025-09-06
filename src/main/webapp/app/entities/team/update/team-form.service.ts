import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITeam, NewTeam } from '../team.model';

/**
 * Вспомогательный тип, делает все свойства типа T необязательными,
 * кроме id (который остается обязательным)
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Тип для входных данных формы.
 * Принимает либо ITeam (для редактирования), либо PartialWithRequiredKeyOf<NewTeam> (для создания)
 */
type TeamFormGroupInput = ITeam | PartialWithRequiredKeyOf<NewTeam>;

/**
 * Тип для значений по умолчанию формы, содержит только id
 */
type TeamFormDefaults = Pick<NewTeam, 'id'>;

/**
 * Тип, описывающий структуру формы (контролы и их типы)
 */
type TeamFormGroupContent = {
  id: FormControl<ITeam['id'] | NewTeam['id']>;
  teamName: FormControl<ITeam['teamName']>;
  employees: FormControl<ITeam['employees']>;
  users: FormControl<ITeam['users']>;
  organization: FormControl<ITeam['organization']>;
};

/**
 * Тип для формы команды, расширяет FormGroup с указанным содержимым
 */
export type TeamFormGroup = FormGroup<TeamFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TeamFormService {
  /**
   * Создает новую форму для команды
   * @param team - начальные данные для формы (по умолчанию {id: null})
   * @returns FormGroup с инициализированными контролами
   */
  createTeamFormGroup(team: TeamFormGroupInput = { id: null }): TeamFormGroup {
    // Объединяем значения по умолчанию с переданными данными
    const teamRawValue = {
      ...this.getFormDefaults(),
      ...team,
    };

    // Создаем и возвращаем FormGroup с тремя контролами
    return new FormGroup<TeamFormGroupContent>({
      id: new FormControl(
        { value: teamRawValue.id, disabled: true }, // id делаем disabled, так как это идентификатор
        {
          nonNullable: true, // Указываем, что значение не может быть null
          validators: [Validators.required], // Добавляем валидатор обязательности
        },
      ),
      teamName: new FormControl(teamRawValue.teamName, {
        validators: [Validators.required], // Название команды обязательно
      }),
      employees: new FormControl(teamRawValue.employees || []), // Сотрудники - массив, по умолчанию пустой
      users: new FormControl(teamRawValue.users || []), // Сотрудники - массив, по умолчанию пустой
      organization: new FormControl(teamRawValue.organization || null),
    });
  }

  /**
   * Извлекает данные из формы
   * @param form - форма команды
   * @returns объект с данными команды (ITeam или NewTeam)
   */
  getTeam(form: TeamFormGroup): ITeam | NewTeam {
    // getRawValue() возвращает значения всех контролов, включая disabled
    return form.getRawValue() as ITeam | NewTeam;
  }

  /**
   * Сбрасывает форму к переданным значениям
   * @param form - форма команды
   * @param team - новые данные для формы
   */
  resetForm(form: TeamFormGroup, team: TeamFormGroupInput): void {
    // Объединяем значения по умолчанию с переданными данными
    const teamRawValue = { ...this.getFormDefaults(), ...team };
    form.reset(
      {
        ...teamRawValue,
        id: { value: teamRawValue.id, disabled: true }, // Снова делаем id disabled
      } as any /* приведение типа для обхода бага в Angular: https://github.com/angular/angular/issues/46458 */,
    );
  }

  /**
   * Возвращает значения формы по умолчанию
   * @returns объект с id = null
   */
  private getFormDefaults(): TeamFormDefaults {
    return {
      id: null,
    };
  }
}
