import struct as st
import itertools as it
import collections
import time
candDict = {}   # 存 Candidate Table 之 Dictionary
comDict = {}    # 存 Combinations 之 Dictionary
combList = []   # 做 Combinations 用之 List
delList = []    # delete sup 不足用之 list
support = 5    # sup:C->L:支持度
Large = 0       # 紀錄 Large Table item 數目
combCount = 0   # 幾個數做 Combinations
end = False     # 終止用
def candToLarge():
    global Large, end
    # 將 Candidate 中不足支持度的key加入List 在依序刪除
    for dictKey in candDict:
        if candDict[dictKey] < support:
            delList.append(dictKey)
    for delKey in delList:
        candDict.pop(delKey, None)
    print("L", candDict.__len__())
    Large = Large + candDict.__len__()
    if candDict.__len__() <= 1:
        end = True
    delList.clear()
def dbCombinations():
    global combCount
    db = combCount + 1
    for combTemp in it.combinations(combList, db):
        if combTemp in candDict:
            candDict[combTemp] += 1
    combList.clear()
def candiadate_K():
    global combCount
    combCount += 1
    if combCount == 1:
        od = collections.OrderedDict(sorted(candDict.items()))
        candDict.clear()
        for key in od:
            candDict[key] = od[key]
        od.clear()

    dC = candDict.__len__()
    dL = candDict.__len__()
    for key1 in candDict:
        for key2 in candDict:
            if dC < dL and dC != 0 and dL != 0:
                # if key1 != key2:
                # print(key1, key2)
                if combCount != 1:
                    combTemp = tuple(sorted(set(key1) | set(key2)))
                else:
                    combTemp = tuple(sorted(set({key1}) | set({key2})))
                combCountTemp = combCount + 1
                if combTemp.__len__() == combCountTemp:
                    if combTemp in comDict:
                        comDict[combTemp] += 1
                    else:
                        comDict[combTemp] = 1
            else:
                dC -= 1
        dL -= 1
        dC = candDict.__len__()

    candDict.clear()
    gradeCount = combCount + 1
    a = 0
    for i in range(gradeCount):
        a += i
    for addKey in comDict:
        # combCountTemp = (combCount + 1) * combCount
        if comDict[addKey] == a:
            candDict[addKey] = 0
    # 讀檔
    with open("T15I7N0.5KD1K.data", "rb") as file:
        count = 0  # 記錄讀了幾次Byte
        cTemp = 0  # 每行多少個Byte，讀真正有用數值之回合數
        while True:
            data = file.read(4)
            count += 1
            if not data:
                break
            value = st.unpack("<i", data)
            if cTemp > 0:
                cTemp -= 1
                combList.append(value[0])
                if cTemp == 0:
                    # combCountTemp = combCount + 1
                    # if count > combCountTemp:
                    dbCombinations()
                    count = 0
            if count == 3:
                cTemp = value[0]
        candToLarge()
def candiadate_1():
    # 讀檔
    with open("T15I7N0.5KD1K.data", "rb") as file:
        count = 0  # 記錄讀了幾次Byte
        cTemp = 0  # 每行多少個Byte，讀真正有用數值之回合數
        while True:
            data = file.read(4)
            count += 1
            if not data:
                break
            value = st.unpack("<i", data)
            if cTemp > 0:
                cTemp -= 1
                count = 0
                if value[0] in candDict:
                    candDict[value[0]] += 1
                else:
                    candDict[value[0]] = 1
            if count == 3:
                cTemp = value[0]
        candToLarge()
def main():
    global end, Large
    candiadate_1()
    while True:
        candiadate_K()
        if end is True:
            break
    print("end ", Large)
if __name__ == "__main__":
    start_time = time.time()
    main()
    print("--- %s seconds ---" % (time.time() - start_time))