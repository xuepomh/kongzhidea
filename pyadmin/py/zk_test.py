# -*- coding: utf-8 -*-
import ZKClient
if __name__ == '__main__':
    client = ZKClient.ZKClient("10.4.28.172:2181,10.4.28.179:2181");
    #print client.get("/thrift/com.rr.publik.service")
    #client.delete("/test")
    #client.create("/test")
    #print client.exists("/test")!=None
    print client.get_children("/thrift/com.rr.publik.service/enable")
