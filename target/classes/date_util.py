# -*- coding: utf-8 -*-

import os
import getopt
import sys
from datetime import datetime, timedelta, date

def cal_time_24h(start_time,end_time,format_str='%Y-%m-%d %H:%M:%S'):
	return (datetime.strptime(end_time, format_str)-datetime.strptime(start_time, format_str)).days

if __name__=='__main__':
	rgs = sys.argv
	opts, args = getopt.getopt(sys.argv[1:], 'hv:', ['help', 'version='])
	start_time = args[0]
	end_time = args[1]
	print(start_time)
	print(end_time)
	print(cal_time_24h(start_time,end_time))
	print("hello tina")
	print("中文")
