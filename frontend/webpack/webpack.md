# Webpack 

## Entry
打包入口，webpack 自动分析入口文件需要的依赖。
```
module.exports = {
  entry: './path/to/my/entry/file.js'
};
```

## Output
配置 webpack 打包输出位置。默认为 ./dist/main.js，也可以通过 output 选项进行配置。
```
const path = require('path');

module.exports = {
  entry: './path/to/my/entry/file.js',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'my-first-webpack.bundle.js'
  }
};
```

## Loaders
通过 loader webpack 可以处理任意类型的文件，将它们处理成 webpack 可以识别的文件类型，并自动分析依赖提供给程序使用。比如将 sass 转成 css 嵌入最后的生成文件。

```
const path = require('path');

module.exports = {
  output: {
    filename: 'my-first-webpack.bundle.js'
  },
  module: {
    rules: [
      { test: /\.txt$/, use: 'raw-loader' }
    ]
  }
};
```
每个 loader 都有两个部分：
- test 属性用于指定哪些文件被该 laoder 处理
- use 指定具体进行加载的 loader

## Plugins
Loaders 用于转化指定类型的文件，plugins 则可以用来执行一些更宽泛的任务，比如bundle 优化，asset 资源文件管理，注入环境变量等。插件需要主动 require 进来，并且使用 npm 安装。大部分插件都会有自己的配置项。

```
const HtmlWebpackPlugin = require('html-webpack-plugin'); //installed via npm
const webpack = require('webpack'); //to access built-in plugins

module.exports = {
  module: {
    rules: [
      { test: /\.txt$/, use: 'raw-loader' }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({template: './src/index.html'})
  ]
};
```
## Mode
可以通过设置 mode 参数为 development, production 或者 none 来控制 webpack 的内部优化机制。默认值为 production。

## Resolve
Resolve 用于配置 modules 如何被 webpack 解析。比如在 es2015 中引入 lodash: `import 'lodash'`，Resolve 可以指定从哪里查找 lodash 引用。 
resolve.alias 可以指定一个模块的别名，比如下面将 `src/utilities/ => Utilities`：
```
module.exports = {
  //...
  resolve: {
    alias: {
      Utilities: path.resolve(__dirname, 'src/utilities/'),
      Templates: path.resolve(__dirname, 'src/templates/')
    }
  }
};
```
这样可以直接使用别名进行引入操作：
```
import Utility from 'Utilities/utility';
```
用于代替：
```
import Utility from '../../utilities/utility';
```
## ResolveLoader
ResolveLoader 和 Resolve 的配置一样，不同的是 Resolve 用于处理 module，resolveLoader 用于处理 loaders。
```
  resolveLoader: {
    modules: ['node_modules', path.resolve(__dirname, 'loaders')],
    alias: {
      'module-loader': path.join(__dirname, 'loaders', 'module-loader.js')
    }
  }
```
## DevServer
DevServer 用于快速开发应用。contentBase 配置指定静态文件的位置。proxy 可以指定哪些请求需要被代理，经常用于代理一些 api 服务。
```
 devServer: {
    contentBase: path.resolve('.'),
    host: 'proxy.xxxx.com', // 开发域名
    proxy: {
      '/servers/hosts': {
        target,
        secure: false,
        changeOrigin: true
      }
    },
    hot: true // 热加载
  }
```

## Externals
Externals 配置项可以指定运行时外部依赖，这些依赖在打包的时候会被 webpack 排除在外。比如可以指定 JQuery 从外部 CDN 获取，而不是打包的时候集成进最终的 bundle 文件。

```
index.html
<script
  src="https://code.jquery.com/jquery-3.1.0.js"
  integrity="sha256-slogkvB1K3VOkzAI8QITxV3VzpOnkeNVsKvtkYLMjfk="
  crossorigin="anonymous">
</script>

webpack.config.js
module.exports = {
  //...
  externals: {
    jquery: 'jQuery'
  }
};
```
对于开发者来说可以在开发的时候正常引入 JQuery 库：
```
import $ from 'jquery';

$('.my-element').animate(/* ... */);
```