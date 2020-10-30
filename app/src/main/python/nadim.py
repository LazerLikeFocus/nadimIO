# -*- coding: utf-8 -*-
"""
Created on Tue Aug 25 16:54:00 2020

@author: Abhishek

appGradle
apply plugin: 'com.chaquo.python'
        sourceSets {
            main {
                python {
                    srcDirs = ["src/main/python"]
                }
            }
        }
        ndk {
            abiFilters "armeabi-v7a", "x86"
        }
        python {
            buildPython "C:/Users/Abhishek/AppData/Local/Programs/Python/Python38/python.exe"
        }

buildGradle
        maven { url "https://chaquo.com/maven" }
        classpath "com.chaquo.python:gradle:8.0.1"

"""

import csv
from os.path import dirname, join
filename_neg = join(dirname(__file__), "negative.csv")
filename_pos = join(dirname(__file__), "positive.csv")
num_neg  = 0
num_pos  = 0


# find the negative word
def find_neg(splitword):
    data_neg = []
    with open(filename_neg) as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            data_neg.append(row)
            
    name = splitword
    
    col = [x[0] for x in data_neg]
    
    if name in col:
        for x in range(0,len(data_neg)):
            if name == data_neg[x][0]:
                print("negative: ", data_neg[x])
                global num_neg
                num_neg += 1
            
    else:
        print("word does not exists")
        

# find the positive word  
def find_pos(splitword):    
    data_pos = []
    with open(filename_pos) as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            data_pos.append(row)
            
    name = splitword
    
    col = [x[0] for x in data_pos]
    
    if name in col:
        for x in range(0,len(data_pos)):
            if name == data_pos[x][0]:
                print("positive: ", data_pos[x])
                global num_pos
                num_pos += 1
                
    else:
        print("word does not exists")
        

# find the words in a sectence

def find(question):
    split =  question.split()
    
    for x in range(0,len(split)):
        find_pos(split[x])
        find_neg(split[x])
    
    
    # now get to cal yes or no
    print(num_pos/ ( num_neg + num_pos) )
    return(num_pos/ ( num_neg + num_pos) )

#abuse abused abuses accurately achievable achievement achievements achievible acumen adaptable adaptive adequate


  