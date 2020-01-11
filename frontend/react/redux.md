# Redux

### state 
react 组件内部状态

### props
react 组件构造参数，初始化的时候传入，多层组件嵌套的时候层层传递。

### store
redux 维护的数据层门面
```
var store = Redux.createStore( reducer function) // 初始化 store 传入 reduer 函数 
store.dispatch({action 对象 }) // 触发一次修改 store 的操作
```

### action
改变 store 状态的指令，枚举
```
const incrementAction = { type: 'INCREMENT' }
```

### reducer
结合 action 实际改变 store 的函数
签名：(state, action) => {...}
比如：
```
function counter(state, action) {
        if (typeof state === 'undefined') {
          return 0
        }
        switch (action.type) {
          case 'INCREMENT':
            return state + 1
          case 'DECREMENT':
            return state - 1
          default:
            return state
        }
      }

```
### 绑定 state 和 react 组件
```
 var store = Redux.createStore(counter) // 创建 store
 function render() {
     valueEl.innerHTML = store.getState().toString() // 使用 store 的状态渲染
  }

  store.subscribe(render) // 每次 store 更新调用
```

### selector:


### container:




