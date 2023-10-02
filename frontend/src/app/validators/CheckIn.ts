import {AbstractControl} from "@angular/forms";

export function checkInCheckOutValidator(control: AbstractControl): { [key: string]: boolean } | null {
  const pattern = /^(\d{2}:\d{2})\s?-\s?(\d{2}:\d{2})$/;

  if (!pattern.test(control.value)) {
    return { invalidCheckRange: true };
  }

  return null;
}
