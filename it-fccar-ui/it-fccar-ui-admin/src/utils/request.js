import axios from 'axios'
import { ElMessage } from 'element-plus'
import JSONBig from "json-bigint"
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分,网关的URL
  baseURL: "http://localhost:10010/fccar",
  // 超时
  timeout: 10000,
  transformResponse:[function (data){
    //处理大数字精度问题  122222222222222222222000
    return JSONBig.parse(data);
  }]
})

// request拦截器
service.interceptors.request.use(config => {
  config.headers['satoken'] = localStorage.getItem("satoken") // 让每个请求携带自定义token 请根据实际情况自行修改
  // get请求映射params参数
  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params);
    url = url.slice(0, -1);
    config.params = {};
    config.url = url;
  }
  return config
}, error => {
    Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;

    if (code === 401) {
      return Promise.reject(new Error('无效的会话，或者会话已过期，请重新登录。'))
    } else if (code === 500) {
      return Promise.reject(new Error(res.message))
    } else if (code !== 200) {
      return Promise.reject('error')
    } else {
      return res.data
    }
  },error => {
    let { message } = error;
    if (message == "Network Error") {
      message = "后端接口连接异常";
    }
    else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    }
    else if (message.includes("Request failed with status code")) {
      message = "系统接口异常[" + message+ "]";
    }
    ElMessage({ type:"error",message: message,})
    return Promise.reject(error)
  }
)


/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    var part = encodeURIComponent(propName) + "=";
    if (value !== null && value !== "" && typeof (value) !== "undefined") {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
            let params = propName + '[' + key + ']';
            var subPart = encodeURIComponent(params) + "=";
            result += subPart + encodeURIComponent(value[key]) + "&";
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&";
      }
    }
  }
  return result
}

export default service
