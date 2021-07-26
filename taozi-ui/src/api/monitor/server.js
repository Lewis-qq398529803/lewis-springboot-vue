import request from '@/com.taozi.utils/request'

// 查询服务器详细
export function getServer() {
  return request({
    url: '/monitor/server',
    method: 'get'
  })
}
