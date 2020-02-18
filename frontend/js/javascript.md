# Javascript

## Function.prototype.bind()
Function.prototype.bind() 
每个函数对象都有一个 bind 方法，bind 可以理解为 attach，即将函数关联到某个对象。bind() 方法创建一个新的函数，在 bind() 被调用时，这个新函数的 this 被指定为 bind() 的第一个参数，而其余参数将作为新函数的参数，供调用时使用。

js 没有域的概念，this 默认是绑定到全局对象中。下面的代码会报错，因为 this 指代的是全局对象，没有 x 属性。
```
const module = {
  x: 42,
  getX: function() {
    return this.x;
  }
}

const unboundGetX = module.getX;
console.log(unboundGetX()); // The function gets invoked at the global scope
// expected output: undefined
```
可以使用 bind 方法将 unboundGetX 方法绑定到 module 对象：
```
const boundGetX = unboundGetX.bind(module);
console.log(boundGetX());
// expected output: 42
```

## Function.prototype.apply()
Apply 方法第一个参数会作为调用函数内部的 this 对象处理，第二个参数为参数数组。
```
const numbers = [5, 6, 2, 3, 7];

var f = function(a, b) {
  console.log(this);
  console.log(a,b);
  console.log(a+b);
}

f.apply({a:1},numbers);
```

## Function.prototype.call()
Call 方法和 apply 方法类似，也是将第一个参数会作为调用函数内部的 this 对象处理，不同的是 call 方法接受一个参数列表而不是一个数组。Call 方法常用于继承：

```
function Product(name, price) {
  this.name = name;
  this.price = price;
}

// Food 继承了 Product
function Food(name, price) {
  // 将 Food 的 this 传入 Product 中
  // 实现类似继承
  Product.call(this, name, price); 
  this.category = 'food';
}

console.log(new Food('cheese', 5).name); 
```

## 原型链

每个函数都有一个 prototype 属性每一个JavaScript对象(null除外)在创建的时候就会与之关联另一个对象，这个对象就是我们所说的原型，每一个对象都会从原型"继承"属性。
```
function Person() {

}

// prototype 是函数才会有的属性
Person.prototype.name = 'Kevin';
var person1 = new Person();
var person2 = new Person();
console.log(person1.name) // Kevin
console.log(person2.name) // Kevin
```
每一个JavaScript对象(除了 null )都具有的一个属性，叫proto，这个属性会指向该对象的原型
```
function Person() {

}
var person = new Person();
console.log(person.__proto__ === Person.prototype); // true
```

下面例子中，我们给实例对象 person 添加了 name 属性，当我们打印 person.name 的时候，结果自然为 Daisy。但是当我们删除了 person 的 name 属性时，读取 person.name，从 person 对象中找不到 name 属性就会从 person 的原型也就是 person.proto ，也就是 Person.prototype中查找，幸运的是我们找到了 name 属性，结果为 Kevin。
```
function Person() {

}

Person.prototype.name = 'Kevin';

var person = new Person();

person.name = 'Daisy';
console.log(person.name) // Daisy

delete person.name;
console.log(person.name) // Kevin
```
JavaScript 默认并不会复制对象的属性，相反，JavaScript 只是在两个对象之间创建一个关联，这样，一个对象就可以通过委托访问另一个对象的属性和函数，所以与其叫继承，委托的说法反而更准确些。