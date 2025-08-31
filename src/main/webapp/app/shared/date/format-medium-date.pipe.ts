import { Pipe, PipeTransform } from '@angular/core';

import dayjs from 'dayjs/esm';

@Pipe({
  name: 'formatMediumDate',
})
export default class FormatMediumDatePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | string | null | undefined): string {
    if (!day) {
      return '';
    }

    // Если это строка, преобразуем в dayjs объект
    if (typeof day === 'string') {
      const dayjsObj = dayjs(day);
      return dayjsObj.isValid() ? dayjsObj.format('D MMM YYYY') : '';
    }

    // Если это уже dayjs объект
    if (dayjs.isDayjs(day)) {
      return day.format('D MMM YYYY');
    }

    return '';
  }
}
