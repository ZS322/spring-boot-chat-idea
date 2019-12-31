//const domain = window.location.protocol + "//" + window.location.host + "/";
//const socketDomain = "ws://"+ window.location.host +"/ws/controller/";
const domain = "https://zhishix11.utools.club/";
const socketDomain = "ws://zhishix11.utools.club/ws/controller/";
Vue.component('chat-list', {
	template: '#chatList',
	props: ['list'],
	computed: {
		last: function() {
			if(this.list.msgList.length == 0){
				return
			}
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
		//搜索结果
		searchRs: {},
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
		//tiao到对应的聊天窗口
		toChat(user){
			//跳到对应的好友窗口
			console.log(user);
			//查找对应的聊天窗口
			var t = this.historyChatList;
			//遍历判断是否存在当前历史聊天
			for (var i = 0; i < t.length; i++) {
				if( user.userId == t[i].senderId){//如果存在
					//跳到tab0
					this.tabIndex = 0;
					this.historyChatListIndex = i;
					//将当前接受者的信息放入message
					this.message.receiverId = user.userId;
					return
				}
			}
			var t2 = {
				msgList:[],
				senderId:user.userId,
				senderUser:user,
			}
			t.unshift(t2);
			//跳到tab0
			this.tabIndex = 0;
			this.historyChatList = t;
			this.historyChatListIndex = 0;
			//将当前接受者的信息放入message
			this.message.receiverId = user.userId;

		},
		//搜索
		search() {
			if (this.searchKW == "") {
				this.searchRs = {};
			}
			var that = this;
			console.log("触发了搜索");
			if (this.tabIndex == 0) { //消息列表搜索
				var t = this.historyChatList;
				for (var i = 0; i < t.length; i++) {
					if(this.searchKW == t[i].senderUser.userName){
						console.log("找到用户");
					}
				}
			} else if (this.tabIndex == 1) { //好友列表搜索
				axios({
					url: domain + "user/userSearch/" + this.searchKW
				}).then((res) => {
					if (res.data.state == 400) { //没找到结果
						that.$message.error('没有这个用户');
					}
					console.log(res.data);
					that.searchRs = res.data;

				})
			}

		},
		//点击添加好友
		addFriend(id) {
			var that = this;
			console.log("点击了添加好友" + id);
			axios({
				url: domain + "user/addFriend/" + id,
				method: "POST"
			}).then((res) => {
				if (res.data.state == 200) {
					that.$message({
						message: '恭喜你，添加好友成功',
						type: 'success'
					});
					var t = that.friendsList;
					t.push(that.searchRs.user);
					that.friendsList = t;
					that.searchKW = "";
					that.searchRs = {};

				} else {
					that.$message.error('添加失败');
				}

			})
		},
		//发送消息
		send() {
			console.log("点击了发送");
			var that = this;
			axios({
				url: domain + "/api/ws/sendOne",
				method: "POST",
				data: that.message,
			}).then(function(res) {
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
				that.message.mContent = '';
			})

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
			this.tabIndex = index;
			this.searchKW = "";
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
