# React Router 配置化
## 配置文件 index.ms ：
```
{
  name: 'marketing-user',
  useReduxForm: false,
  routes: [
    {
      path: 'user/user-journey', // 路由路径
      container: 'page/JourneyUpdate', // 路由需要加载的组件
      isFull: true,
      store: 'page/JourneyUpdate/store' // store 路径
    }, {
      path: 'user/overview',
      container: 'page/JourneyOverview',
      isFull: true,
      store: 'page/JourneyUpdate/store'
    }
  ]
}
```

## 使用 webpack load 配置，翻译成加载 js 代码：
```
  resolveLoader: {
    alias: {
      'module-loader': path.join(__dirname, 'loaders', 'module-loader.js')
    }
  }
```

## 帮助函数：
```
// 合并传入的 reducers
export const makeRootReducer = (asyncReducers) => {
  var store = combineReducers({
    // Sync Reducers
    ...reducers,
    // Async Reducers
    ...asyncReducers
  });
  store.reducers = store.reducers || reducers;
  store.asyncReducers = store.asyncReducers || {};
  return store;
};

// 向传入的 store 注入 injectObject
export const injectReducer = (store, injectObject, forceUpdate = false) => {
  store.asyncReducers = forceUpdate ? assign(store.asyncReducers, injectObject) : defaults(store.asyncReducers, injectObject);
  store.replaceReducer(makeRootReducer(store.asyncReducers));
};
```

## 组装 reducer：
```
import { flatten } from 'lodash';
import { injectReducer } from 'store/reducers';
import inAppMessage from 'modules/inAppMessage/index.ms';

// 使用 injectReducer 向 store 注入对应的 reducer
export default (store) => flatten([
  inAppMessage(store, injectReducer)
]);
```

## 注入路由：
```
import loadModules from './modules';

export const store = createStore(); // 创建 store
let routes = loadModules(store); // 注入 reducer 并生成对应的路由
      
    <Route
        path='/projects/:project_id/mp'
        component={App}
        childRoutes={routes}
        onChange={handleRouteChange}
        />
    </Router>
```

## 附录
### loader 源码
``` module-loader.js
const serialize = require('./serialize');
const deserialize = (serializedJavascript) => {
  return eval('(' + serializedJavascript + ')');
}

const getAsyncRoutes = (routes, config) => routes.map((route) =>  Object.assign({}, route, {
  childRoutes: transformRoutes(config, route.childRoutes),
  getComponent: `***(nextState, cb) =>  {
    require.ensure([], require => {
      const routeContainer = require('./${route.container}').default;`
      + ((config.store || route.store) ? `
      const routeStore = require('./${route.store || config.store}').default;
      if (routeStore.reducer) {
        injectReducer(store, { [routeStore.SCOPENAME]: routeStore.reducer }, routeStore.forceUpdate);
      }
      if (routeStore.sagas) {
        const sagaMiddleware = require('store/createStore').sagaMiddleware;
        routeStore.sagas.forEach((saga) => {
          if (!saga.hasRun) {
            saga.hasRun = true;
            sagaMiddleware.run(saga);
          }
        });
      }` : '')
      + (config.useReduxForm ? `const fmReducer = require('redux-form').reducer;injectReducer(store, { form: fmReducer });` : '')
      + `
      const resourceAuths = [];
      nextState.routes.map((r) => {
        if (r.auth && r.auth.resource) {
          const routeResourceAuths = r.auth.resource;
          routeResourceAuths.forEach(resourceAuth => {
            resourceAuths.push([resourceAuth[0], resourceAuth[1], nextState.params[resourceAuth[2]]])
          });
        }
      });
      let authPromise = Promise.resolve(true);
      if (resourceAuths.length) {
        const http = require('utils/http').default;
        const resourceConfig = require('modules/core/resourceConfig').default;
        authPromise = Promise.all(resourceAuths.map((resourceAuth) => {
          return window.store.dispatch(resourceConfig[resourceAuth[1]].service.actions.getById(resourceAuth[2])).then((data) => {
            return data.acl && data.acl.actions && data.acl.actions.indexOf(resourceAuth[0]) >= 0;;
          });
        })).then(cans => cans.every(can => can))
        .catch(error => {
          if (error.httpStatus === 404) {
            return '404';
          }
          return 'error';
        });
      }
      authPromise.then((can) => {
        if (can === '404') {
          const NotFound = require('modules/core/containers/NotFound').default;
          cb(null, NotFound);
        } else if (can === 'error') {
          const Error = require('modules/core/containers/Error').default;
          cb(null, Error);
        } else if (can) {
          cb(null, routeContainer);
        } else {
          const Forbidden = require('modules/core/containers/Forbidden').default;
          cb(null, Forbidden);
        }
      })
  })}***`
}));

const getSyncRoutes = (routes, config) => routes.map((route) =>  Object.assign({}, route, {
  childRoutes: transformRoutes(config, route.childRoutes),
  getComponent: `***(nextState, cb) =>  {
      const routeContainer = require('./${route.container}').default;`
      + (config.store ? `
      const routeStore = require('./${config.store}').default;
      if (routeStore.reducer) {
        injectReducer(store, { [routeStore.SCOPENAME]: routeStore.reducer });
      }
      if (routeStore.sagas) {
        const sagaMiddleware = require('store/createStore').sagaMiddleware;
        routeStore.sagas.forEach((saga) => {
          if (!saga.hasRun) {
            saga.hasRun = true;
            sagaMiddleware.run(saga);
          }
        });
      }` : '')
      + (config.useReduxForm ? `const fmReducer = require('redux-form').reducer;injectReducer(store, { form: fmReducer });` : '')
      + `cb(null, routeContainer);
  }***`
}));

const transformRoutes = (config, routes) => {
  if (!routes) {
    return undefined;
  }
  return  getAsyncRoutes(routes, config);
};

module.exports = function (source, map) {
  this.cacheable();

  const config = deserialize(source);
  const injectRoute = transformRoutes(config, config.routes);
  const result = ('export default  (store, injectReducer) => ' + serialize(injectRoute)).replace(/\"\*\*\*|\*\*\*\"|\\n/g, "");

  this.callback(null, result, map);
}
```
### 序列化代码
```serialize.js
'use strict';

// Generate an internal UID to make the regexp pattern harder to guess.
var UID                 = Math.floor(Math.random() * 0x10000000000).toString(16);
var PLACE_HOLDER_REGEXP = new RegExp('"@__(F|R|D)-' + UID + '-(\\d+)__@"', 'g');

var IS_NATIVE_CODE_REGEXP = /\{\s*\[native code\]\s*\}/g;


module.exports = function serialize(obj, options) {
    options || (options = {});

    // Backwards-compatability for `space` as the second argument.
    if (typeof options === 'number' || typeof options === 'string') {
        options = {space: options};
    }

    var functions = [];
    var regexps   = [];
    var dates     = [];

    // Returns placeholders for functions and regexps (identified by index)
    // which are later replaced by their string representation.
    function replacer(key, value) {
        if (!value) {
            return value;
        }

        // If the value is an object w/ a toJSON method, toJSON is called before
        // the replacer runs, so we use this[key] to get the non-toJSONed value.
        var origValue = this[key];
        var type = typeof origValue;

        if (type === 'object') {
            if(origValue instanceof RegExp) {
                return '@__R-' + UID + '-' + (regexps.push(origValue) - 1) + '__@';
            }

            if(origValue instanceof Date) {
                return '@__D-' + UID + '-' + (dates.push(origValue) - 1) + '__@';
            }
        }

        if (type === 'function') {
            return '@__F-' + UID + '-' + (functions.push(origValue) - 1) + '__@';
        }

        return value;
    }

    var str;

    // Creates a JSON string representation of the value.
    // NOTE: Node 0.12 goes into slow mode with extra JSON.stringify() args.
    if (options.isJSON && !options.space) {
        str = JSON.stringify(obj);
    } else {
        str = JSON.stringify(obj, options.isJSON ? null : replacer, options.space);
    }

    // Protects against `JSON.stringify()` returning `undefined`, by serializing
    // to the literal string: "undefined".
    if (typeof str !== 'string') {
        return String(str);
    }

    if (functions.length === 0 && regexps.length === 0 && dates.length === 0) {
        return str;
    }

    // Replaces all occurrences of function, regexp and date placeholders in the
    // JSON string with their string representations. If the original value can
    // not be found, then `undefined` is used.
    return str.replace(PLACE_HOLDER_REGEXP, function (match, type, valueIndex) {
        if (type === 'D') {
            return "new Date(\"" + dates[valueIndex].toISOString() + "\")";
        }

        if (type === 'R') {
            return regexps[valueIndex].toString();
        }

        var fn           = functions[valueIndex];
        var serializedFn = fn.toString();

        if (IS_NATIVE_CODE_REGEXP.test(serializedFn)) {
            throw new TypeError('Serializing native function: ' + fn.name);
        }

        return serializedFn;
    });
}

```
### 转化后的代码
```
export default(store, injectReducer) = >[{
  "path": "user/user-journey",
  "container": "page/JourneyUpdate",
  "isFull": true,
  "store": "page/JourneyUpdate/store",
  "getComponent": (nextState, cb) = >{
      require.ensure([], require = >{
          const routeContainer = require('./page/JourneyUpdate').default;
          const routeStore = require('./page/JourneyUpdate/store').default;
          if (routeStore.reducer) {
              injectReducer(store, { [routeStore.SCOPENAME] : routeStore.reducer
              },
              routeStore.forceUpdate);
          }
          if (routeStore.sagas) {
              const sagaMiddleware = require('store/createStore').sagaMiddleware;
              routeStore.sagas.forEach((saga) = >{
                  if (!saga.hasRun) {
                      saga.hasRun = true;
                      sagaMiddleware.run(saga);
                  }
              });
          }
          const resourceAuths = [];
          nextState.routes.map((r) = >{
              if (r.auth && r.auth.resource) {
                  const routeResourceAuths = r.auth.resource;
                  routeResourceAuths.forEach(resourceAuth = >{
                      resourceAuths.push([resourceAuth[0], resourceAuth[1], nextState.params[resourceAuth[2]]])
                  });
              }
          });
          let authPromise = Promise.resolve(true);
          if (resourceAuths.length) {
              const http = require('utils/http').default;
              const resourceConfig = require('modules/core/resourceConfig').default;
              authPromise = Promise.all(resourceAuths.map((resourceAuth) = >{
                  return window.store.dispatch(resourceConfig[resourceAuth[1]].service.actions.getById(resourceAuth[2])).then((data) = >{
                      return data.acl && data.acl.actions && data.acl.actions.indexOf(resourceAuth[0]) >= 0;;
                  });
              })).then(cans = >cans.every(can = >can)).
              catch(error = >{
                  if (error.httpStatus === 404) {
                      return '404';
                  }
                  return 'error';
              });
          }
          authPromise.then((can) = >{
              if (can === '404') {
                  const NotFound = require('modules/core/containers/NotFound').
              default;
                  cb(null, NotFound);
              } else if (can === 'error') {
                  const Error = require('modules/core/containers/Error').
              default;
                  cb(null, Error);
              } else if (can) {
                  cb(null, routeContainer);
              } else {
                  const Forbidden = require('modules/core/containers/Forbidden').
              default;
                  cb(null, Forbidden);
              }
          })
      })
  }
}]
```