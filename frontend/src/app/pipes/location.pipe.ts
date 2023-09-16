import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'locationFilter'
})
export class LocationPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (!value) return [];
    if (!args) return [];

    const filteredItems = value.filter((item: any) => {
      return item.name.toLowerCase().startsWith(args.toLowerCase());
    })

    return filteredItems.slice(0, 3);
  }

}
