import {Notification} from 'element-ui';
import axios from 'axios'

axios.defaults.timeout = 200000;                        //响应时间
axios.defaults.headers.common['Authorization'] = '';
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';           //配置请求头
axios.defaults.baseURL = process.env.NODE_ENV == 'development' ? '/api' : '';                   //配置接口地址

//POST传参序列化(添加请求拦截器)
axios.interceptors.request.use((config) => {
  //在发送请求之前做某件事
  config.headers['X-Requested-With'] = 'XMLHttpRequest'
  if (config.method === 'post') {
    config.data = JSON.stringify(config.data)
  }
  return config;
}, (error) => {
  Notification.error({
    title: '错误',
    message: '出现异常！'
  });
  return Promise.reject(error);
});

//返回状态判断(添加响应拦截器)
axios.interceptors.response.use((res) => {
  var resData = res.data
  //对响应数据做些事
  if( resData && resData.status == 401) {
    window.location.href = resData.url
  }
  return res;
}, (error) => {
  Notification.error({
    title: '错误',
    message: '网络异常！'
  });
  return Promise.reject(error);
});

var method = {}

//返回一个Promise(发送post请求)
method.fetch = (url, params) => {
  return new Promise((resolve, reject) => {
    axios.post(url, params)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err);
      })
      .catch((error) => {
        reject(error)
      })
  })
}

function downloadFile(res, fileName) {
  const content = res.data
  const blob = new Blob([content])
  if ('download' in document.createElement('a')) { // 非IE下载
    const elink = document.createElement('a')
    elink.download = fileName
    elink.style.display = 'none'
    elink.href = URL.createObjectURL(blob)
    document.body.appendChild(elink)
    elink.click()
    URL.revokeObjectURL(elink.href) // 释放URL 对象
    document.body.removeChild(elink)
  } else { // IE10+下载
    navigator.msSaveBlob(blob, fileName)
  }
}

//返回一个Promise(发送post请求)
method.download = (url, params, fileName) => {
  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: url,
      data: params,
      responseType: 'blob'
    }).then(response => {
      downloadFile(response, fileName)
      resolve(response);
    }).catch((error) => {
      reject(error)
    })
  })
}

//返回一个Promise(发送post请求)
method.post = (url, params) => {
  return new Promise((resolve, reject) => {
    axios.post(url, params)
      .then(response => {
        resolve(response);
      }, err => {
        reject(err);
      })
      .catch((error) => {
        reject(error)
      })
  })
}

//返回一个Promise(发送post请求)
method.get = (url, params) => {
  return new Promise((resolve, reject) => {
    axios.get(url)
      .then(response => {
        resolve(response);
      }, err => {
        reject(err);
      })
      .catch((error) => {
        reject(error)
      })
  })
}

// method.axios = axios

export default method
