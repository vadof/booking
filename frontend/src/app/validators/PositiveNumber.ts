import {AbstractControl} from "@angular/forms";

export function positiveNumberValidator(control: AbstractControl): { [key: string]: boolean } | null {
  const value: number = +control.value;
  if (!value || value <= 0) {
    return {negativeNumber: true};
  }

  return null;
}
