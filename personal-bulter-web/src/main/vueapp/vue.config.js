module.exports = {
  outputDir: '../resources/public',
  assetsDir: 'static',
  productionSourceMap: false,
  devServer: {
    port: 8081,
    disableHostCheck: true,
    host: 'sale.test.yunshanmeicai.com',
    proxy: {
      '/api': {
        target: 'http://sale.test.yunshanmeicai.com:8082/',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}