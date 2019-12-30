//const domain = window.location.protocol + "//" + window.location.host + "/";
//const socketDomain = "ws://"+ window.location.host +"/ws/controller/";
const domain = "https://zhishix11.utools.club/";
const socketDomain = "ws://zhishix11.utools.club/ws/controller/";
Vue.component('chat-list', {
	template: '#chatList',
	props: ['list'],
	computed: {
		last: function() {
			var i = this.list.msgList.length;
			console.log(i);
			console.log(this.list.msgList[i - 1]);
			return this.list.msgList[i - 1].mContent;
		}
	}
})
Vue.component('bubble-box', {
	template: '#bubble',
	props: ['ismy', 'content', 'user']
})

var vm = new Vue({
	el: '#app',
	data: {
		//表情弹窗
		visible: false,
		//搜索关键词
		searchKW: '',
		//聊天内容列表
		charList: [],
		//历史聊天列表
		historyChatList: '',
		//当前聊天
		historyChatListIndex: -1,
		//好友列表
		friendsList: [],
		//搜索框关键词
		keyWord: '',
		//当前用户信息
		userInfo: {},
		message: {
			//发送内容的类型
			mType: 0,
			//发送的内容
			mContent: '',
			//接受者的id
			receiverId: 0,
			//发送人的id
			senderId: 0,
		},
		//Tab信息
		tabList: [{
			id: 0,
			icon: 'el-icon-help',
			name: '消息'
		}, {
			id: 1,
			icon: 'el-icon-user',
			name: '好友'
		}, {
			id: 2,
			icon: 'el-icon-circle-close',
			name: '退出'
		}],
		//当前Tab
		tabIndex: 0,
	},
	created() {
		this.getUserInfoForStorage(); //本地缓存中获取当前登录信息
		this.createSockte();
		this.getFriendList();
	},
	methods: {
		//发送消息
		send() {
			console.log("点击了发送");
			var that = this;
			axios({
				url: domain + "/api/ws/sendOne",
				method: "POST",
				data: that.message,
			}).then(function(res) {
				// console.log("发送成功");
				// //重新生成
				// var temp = {
				// 	//发送内容的类型
				// 	mType: that.message.mType,
				// 	//发送的内容
				// 	mContent: that.message.mContent,
				// 	//接受者的id
				// 	mReceiverId: that.message.receiverId,
				// 	//发送人的id
				// 	mSenderId: that.message.senderId,
				// };
				// var t2 = that.charList;
				// that.charList = t2.push(temp);
			})
			console.log("发送成功");
			//重新生成
			var temp = {
				//发送内容的类型
				mType: that.message.mType,
				//发送的内容
				mContent: that.message.mContent,
				//接受者的id
				mReceiverId: that.message.receiverId,
				//发送人的id
				mSenderId: that.message.senderId,
				mCreateTime: that.isGetDate(),
			};
			console.log(that.isGetDate());
			var t2 = that.charList;
			t2.msgList.push(temp);
			that.charList = t2;
		},
		//重本地缓存中获取当前登录信息
		getUserInfoForStorage() {
			var user = sessionStorage.getItem("user");
			this.userInfo = JSON.parse(user);
			//将当前用户的id 保存到要发送的id中
			this.message.senderId = this.userInfo.userId;
		},
		//点击列表
		changeHList(index) {
			this.historyChatListIndex = index;
			this.charList = this.historyChatList[index];
			//将当前接受者的信息放入message
			this.message.receiverId = this.historyChatList[index].senderId;
		},
		//加载当前登录用户历史消息
		getHList() {
			var that = this;
			axios({
				url: domain + 'msg/findList',
				withCredentials: true,
			}).then(function(res) {
				console.log(res);
				console.log(res.data);
				var temp = res.data;
				//开始从缓存的好友列表中取到历史消息中的用户的信息
				for (var i = 0; i < temp.length; i++) {
					//从缓存中获取到对应id的用户信息
					temp[i].senderUser = JSON.parse(sessionStorage.getItem(temp[i].senderId));
				}
				that.historyChatList = temp;
				console.log(temp);
			})
		},

		//加载当前登录用户好友列表
		getFriendList() {
			var that = this;
			axios({
				url: domain + 'friend/findList',
				withCredentials: true,
			}).then(function(res) {
				console.log(res);

				console.log(res.data);
				var temp = res.data.data;
				that.friendsList = temp;
				//将好友信息缓存到本地
				for (var i = 0; i < temp.length; i++) {
					console.log(temp[i].userId);
					sessionStorage.setItem(temp[i].userId, JSON.stringify(temp[i]));
				}
				//获取未读的消息列表
				that.getHList();
			}).catch(() => {
				that.$confirm('您还未登录,是否回到登录页?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					that.$message({
						type: 'success',
						message: '正在跳转登录页!'
					});
					window.location.href = "login.html";
				}).catch(() => {
					that.$message({
						type: 'info',
						message: '正在跳转首页'
					});
					window.location.href = "/";
				});
			})
		},
		//点击Tab
		changeTab(index) {
			if (index == 1) {
				this.historyChatListIndex = -1;
			}
			if (index == 2) {
				//开始退出登录
				//调用退出接口
				this.logOut();

			}
			this.tabIndex = index
		},
		logOut() {
			axios({
				url: domain + "user/exit"
			}).then(function(res) {
				if (res.status == 200) {
					window.location.href = "login.html";
				}
			})
		},
		createSockte() { //开始创建socket链接
			var that = this;
			var Socket = new WebSocket(socketDomain + this.userInfo.userId);
			//连接打开事件
			Socket.onopen = function() {
				//成功则提示登录成功
				// that.$notify({
				// 	title: '登录成功',
				// 	message: '赶紧开始聊起来吧!',
				// 	type: 'success'
				// });
				console.log("Socket 已打开");
				//Socket.send("消息发送测试(From Client)");
			};
			//收到消息事件
			Socket.onmessage = function(msg) {
				console.log(msg.data);
			};
			//连接关闭事件
			Socket.onclose = function() {
				console.log("Socket已关闭");
			};
			//发生了错误事件
			Socket.onerror = function() {
				alert("Socket发生了错误");
			}

			//窗口关闭时，关闭连接
			window.unload = function() {
				socket.close();
			};
		},

		isGetDate() {

			var myDate = new Date();

			//获取当前年
			var year = myDate.getFullYear();

			//获取当前月
			var month = myDate.getMonth() + 1;

			//获取当前日
			var date = myDate.getDate();
			var h = myDate.getHours(); //获取当前小时数(0-23)
			var m = myDate.getMinutes(); //获取当前分钟数(0-59)
			//获取当前时间
			var now = year + '-' + this.conver(month) + "-" + this.conver(date) + " " + this.conver(h) + ':' + this.conver(m);
			return now;
		},
		//日期时间处理
		conver(s) {
			return s < 10 ? '0' + s : s;
		}
	}
})
