/**
* 显示消息提示框
* @param content 提示的标题
*/
export function showToast(content) {
  uni.showToast({
    icon: 'none',
    title: content
  })
}

/**
* 显示模态弹窗
* @param content 提示的标题
*/
export function showConfirm(content) {
  return new Promise((resolve, reject) => {
    uni.showModal({
      title: '提示',
      content: content,
      cancelText: '取消',
      confirmText: '确定',
      success: function(res) {
        resolve(res)
      }
    })
  })
}

//把20230314转换为 2023-03-14
export function dateformart(timeStr){
	if(timeStr.length != 8)return;
	let y = timeStr.substr(0,4);
	let m = timeStr.substr(4,2)
	let d = timeStr.substr(6,2)
	return y+"-"+m+"-"+d;
}