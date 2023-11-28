# ----------------------------------------------------- 网关配置文件 -----------------------------------------------------
port=10000
proxy.forwarded=true
max_body_size=100000000
auth.cookie=uni_facade_token
auth.token.header=uni_facade_token
log.filter=/status,/prometheus
# 跨域选项
options.allow.path=/**
options.allow.origin=*
options.allow.headers=*
options.allow.methods=*
options.max.age=600

