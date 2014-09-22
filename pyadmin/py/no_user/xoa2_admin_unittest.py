import unittest
from xoa2_admin_new import Xoa2Admin

class listtest(unittest.TestCase):

  ## 
  def setUp(self):
    old_zk_addr = "10.11.21.185:2181,10.11.21.186:2181,10.3.18.186:2181/xoa2"
    new_zk_addr = "10.11.18.16:2181/test"
    self.xoa2admin = Xoa2Admin(old_zk_addr=old_zk_addr, new_zk_addr=new_zk_addr)

  #
  def tearDown(self):
    pass

  #list
  def testList(self):
    self.xoa2admin.do_list('','')

  #list serviceId
  def testListService(self):
    self.xoa2admin.do_list('longzhe.test.xoa.renren.com', '') # both in old and new zk
    #self.xoa2admin.do_list('longzhe.test', '') # both not in old and new zk
    #self.xoa2admin.do_list('unittest.test.xoa.renren.com', '') # noly in old zk
    #self.xoa2admin.do_list('login.demo.xoa.renren.com', '') # noly in new zk

  #list serviceId endpoint
  def testListServiceEndpoint(self):
    self.xoa2admin.do_list('longzhe.test.xoa.renren.com', '10.2.16.51:9090') # both in old and new zk
    #self.xoa2admin.do_list('longzhe.test.xoa.renren.com', '10.2.16.51:8888') # both not in old and new zk
    #self.xoa2admin.do_list('unittest.test.xoa.renren.com', '127.0.0.1:10000') # noly in old zk
    #self.xoa2admin.do_list('login.demo.xoa.renren.com', '10.4.1.112:9090') # noly in new zk


if __name__ == '__main__':
	unittest.main()