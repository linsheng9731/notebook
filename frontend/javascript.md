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