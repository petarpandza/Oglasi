import { City } from "./city";

export class adInfo {
  id: number;
  title: string;
  shortDesc: string;
  longDesc: string;
  city: City;
  type: number;
  state: number;
  price: number;
  specs: Map<string, string>;
  pictures: string[];



  constructor(id:number, title: string, shortDesc: string, longDesc: string, city: City, type: number, state: number, price: number, specs: Map<string, string>, pictures: string[]) {
    this.id = id;
    this.title = title;
    this.shortDesc = shortDesc;
    this.longDesc = longDesc;
    this.city = city;
    this.type = type;
    this.state = state;
    this.price = price;
    this.specs = specs;
    this.pictures = pictures;
  }
}
