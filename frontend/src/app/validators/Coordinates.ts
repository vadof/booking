import {AbstractControl} from "@angular/forms";

export function coordinatesValidator(control: AbstractControl): { [key: string]: boolean } | null {
  const pattern = /^[-+]?\d{1,2}\.\d+\s*,\s*[-+]?\d{1,3}\.\d+$/;

  if (!pattern.test(control.value)) {
    return {invalidCoordinates: true};
  }

  return null;
}
