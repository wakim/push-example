window.SH = {};

window.SH.push = (
	function() {
		
		function PushConnector(){}
		
		PushConnector.prototype.init = function(){};
		PushConnector.prototype.connect = function(channel, event, onMessage, clientEnabled, onError){};
		PushConnector.prototype.disconnect = function(channel) {};
		PushConnector.prototype.publish = function(channel, event, message) {};
		PushConnector.prototype.uuid = function() { return null; };
		
		var pusherConnector = (function() {
			var connector = new PushConnector();
			
			connector.init = function() {
				connector.pusher = new Pusher('74c8e3a5bc3f420957e2', {authEndpoint: '/pusher/auth'});
			};
			
			connector.connect = function(channel, event, onMessage, clientEnabled, onError) {
				connector.channel = connector.pusher.subscribe('private-' + channel);
				connector.channel.bind(event, onMessage);
				
				if(clientEnabled) {
					connector.channel.bind('client-' + event, onMessage);
				}
				
				connector.channel.bind('pusher:subscription_error', onError);
			};
			
			connector.disconnect = function(channel) {
				connector.pusher.disconnect();
			};
			
			connector.publish = function (channel, event, message) {
				connector.channel.trigger('client-' + event, message);
			};
			
			connector.uuid = function() {
				return connector.pusher.connection.socket_id;
			};
			
			return connector;
		})();
		
		var pubnubConnector = (function() {
			var connector = new PushConnector();
			
			connector.init = function() {
				PUBNUB.init({
					subscribe_key : 'sub-c-c6932830-4d93-11e3-86a5-02ee2ddab7fe'
					,publish_key : 'pub-c-35e68a47-e48e-4f04-90eb-c2fa4c94d5e7'
				});
			};
			
			connector.connect = function(channel, event, onMessage, clientEnabled, onError) {
				PUBNUB.subscribe({
					channel: channel,
					message: onMessage,
					error: onError,
					restore: true
				});
			};
			
			connector.disconnect = function(channel) {
				PUBNUB.unsubscribe({
					channel : channel
				});
			};
			
			connector.publish = function(channel, event, message) {
				PUBNUB.publish({
					channel: channel,
					message: message
				});
			};
			
			connector.uuid = function() {
				return PUBNUB.uuid();
			};
			
			return connector;
		})();
		
		var initialized = false;
		
		var pushConnector = pusherConnector;
		
		var init = function(options) {
			if(! initialized) {
				pushConnector.init();
			}
			
			var WidgetVar = function () {
				this.connect = function() {
					var onMessage = function(data) {
						
						var noReply = ((typeof data['no-reply']) != 'undefined' && data['no-reply']);
						var uuid = pushConnector.uuid();
						
						if(data.uuid != null && uuid != null && data.uuid === uuid) {
							return;
						}
						
						if(! noReply && options.onMessage) {
							options.onMessage.call(this, data.message);
						}
						
						if(! noReply && options.behaviorOnMessage) {
							options.behaviorOnMessage.call(this, {'data': data.message, 'uuid': uuid});
						}
						
						console.log(data);
					};
					
					var onError = function() {
						if(options.onError) {
							options.onError.call(this);
						}
						
						if(options.behaviorOnError) {
							options.behaviorOnError.call(this);
						}
					};
					
					pushConnector.connect(options.channel, options.event || 'sh-all', onMessage, (typeof options.onMessage === 'function'), onError);
				};
				
				this.disconnect = function() {
					pushConnector.disconnect(options.channel);
				};
				
				this.publish = function(message) {
					var uuid = pushConnector.uuid();
					pushConnector.publish(options.channel, options.event || 'sh-all', {'message': message, 'uuid': uuid});
				};
			};
			
			var wv = new WidgetVar();
			
			if(options.widgetVar) {
				window[options.widgetVar] = wv;
			}
			
			if(options.autoConnect) {
				$(function() {
					wv.connect();
				});
			}
		};
		
		return {init : init};
	}
)();