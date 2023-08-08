const HtmlWebPackPlugin = require("html-webpack-plugin");
const Dotenv = require('dotenv-webpack');
const path = require('path');
module.exports = {
  entry: './src/index.js',
  output: {
    filename: '[name].js',
    path: __dirname + '/dist',
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader"
        }
      },
      {
        test: /\.(jpg|jpe?g|png|gif|svg)$/i, 
        loader: "url-loader?name=/images/[name].[ext]"
      },
      {
        test:/\.css$/,
        use:['style-loader','css-loader']
      }
    ]
  },
  optimization: {
    splitChunks: {
      chunks: 'all',
    },
  },
  devServer: {
    historyApiFallback: true,
    contentBase: path.resolve(__dirname, 'dist/index.html'),
    port: 3000
  },
  plugins: [
    new HtmlWebPackPlugin({
      template: "./src/index.html",
      favicon: "./src/images/favicon.ico"
    }),
    new Dotenv()
  ]
};