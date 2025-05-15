export class adInfo {
  name: string;
  description: string;
  city: string;
  type: string;
  state: string;
  price: number;
  specs: Map<string, string>;
  pictures: string[];



  constructor(name: string, description: string, city: string, type: string, state: string, price: number, specs: Map<string, string>, pictures: string[]) {
    this.name = name;
    this.description = description;
    this.city = city;
    this.type = type;
    this.state = state;
    this.price = price;
    this.specs = specs;
    this.pictures = pictures;
  }
}
