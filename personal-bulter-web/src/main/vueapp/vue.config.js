module.exports = {
  outputDir: '../resources/public',
  assetsDir: 'static',
  productionSourceMap: false,
  devServer: {
    port: 8011,
    disableHostCheck: true,
    host: '127.0.0.1',
    proxy: {
      '/bulter': {
        target: 'http://localhost:8082/',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
