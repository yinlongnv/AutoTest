import copy
import itertools
import sys


class PairWiseUtils:
    # 1、笛卡尔积 对参数分组全排列
    @staticmethod
    def product(params):
        new_list = []
        for x in eval('itertools.product' + str(tuple(params))):
            new_list.append(x)
        return new_list

    # 2、对笛卡尔积处理后的二维原始数据进行N配对处理，得到Pairwise计算之前的数据
    @staticmethod
    def get_pairs_list(product_list, pair_len):
        pw_list = []
        for i in product_list:
            sub_temp_list = []
            # itertools.combinations迭代器
            for sub_list in itertools.combinations(i, pair_len):
                sub_temp_list.append(sub_list)
            pw_list.append(sub_temp_list)
        return pw_list

    # 3、进行pairwise算法计算
    def pairwise(self, all_param, pair_len):
        product_list = self.product(all_param)  # product_list笛卡尔积全排列组合的测试用例
        # self.pprint(product_list)
        # print ('笛卡尔积全排列组合数量：',len(product_list),'--'*11)
        pw_list = self.get_pairs_list(product_list, pair_len)  # pw_list对测试用例结对拆分后的二维列表
        sub_list_len = len(pw_list[1])  # sub_list_len每个测试用例拆分后一维列表长度
        flag = [0] * sub_list_len       # 一条测试用例拆分后，每个结对在二维列表同位置上是否有相同值，其标识列表为flag，flag长度根据sub_list_len改变
        temp_list = copy.deepcopy(pw_list)  # 【有效组】的原始值与pw_list相同
        del_menu = []  # 无效测试用例编号列表
        hold_menu = []  # 有效测试用例编号列表
        # self.pprint (pw_list)
        # print ('--'*7,'有效测试用例计算结果','--'*7)
        for cow in pw_list:  # 一维遍历，等同于二维数组按照行遍历
            for column in cow:  # 二维遍历，等同二维数组中任意一行按照‘列’横向遍历
                for temp_list_cow in temp_list:  # 【有效组 = temp_list】中按照行，从上至下遍历
                    x = cow.index(column)  # 待校验元素的横坐标
                    # y = pw_list.index(cow)  # 待校验元素的纵坐标
                    # 有效组中行不能是当前要对比元素所在的行，
                    # 且带对比元素与【有效组】的行temp_list_cow中的坐标x元素相同，
                    # 则认为对比成功，跳出第三层for循环。
                    if temp_list_cow != cow and column == temp_list_cow[x]:
                        flag[x] = 1  # 1表示对比成功
                        break
                    else:  # 对比不成功，需要继续第三层for循环对比
                        flag[x] = 0  # 0表示对比不成功
            if 0 not in flag:  # 如果对比列表中都是1，表明该行的所有结对都在同列的对应位置找到了
                num = pw_list.index(cow)
                del_menu.append(num)  # 记录该无用用例所在总列表pw_list中的位置
                temp_list.remove(cow)  # 有效组中删除该无用用例，保持有效性
            else:  # 如果有0出现在对比列表flag中，则表示该行所有结对组并未全在同列对应位置找到，此用例为有效用例应保留
                num2 = pw_list.index(cow)
                hold_menu.append(num2)  # 记录有效用例在总列表pw_list中的坐标
        # print ('保留元素列表：%s \n匹配重复元素列表：%s'%(hold_menu, del_menu))
        hold_params_list = []
        for item in hold_menu:
            hold_params_list.append(product_list[item])
        return hold_params_list


# 从txt中提取生成的参数list数据
def get_params_from_file(file_path):
    f = open(file_path)
    lines = f.read().split("\n")
    result = []
    for line in lines[:-1]:
        result.append(eval(line))
    return result


# 从txt中提取参数名
def get_keys_from_file(keys_path):
    f = open(keys_path)
    lines = f.read().split("\n")
    result = []
    for line in lines[:-1]:
        result.append(line)
    return result


# 把参数名和参数值处理成map结构
def data_process(data, kys):
    result = ''
    for one_data in data:
        obj = {}
        index = 0
        for key in kys:
            if index < len(kys):
                obj[key] = one_data[index]
                index += 1
        result = result + ";" + str(obj)
    print(result)


if __name__ == '__main__':
    # all_params = [['M', 'O', 'P'], ['W', 'L', 'I'], ['C', 'E']]
    # all_params = [['M', 'O', 'T'], ['L', 'I', 'T'], ['s', 'T', 'E', 'K'], [1, 3], ['Yes', 'No'], ['666', '']]
    # all_params = [['r', 'pwS3lbOlFN', 'a', 'Wb3HqmQuREF'], ['zndiuL', 'w!EonPZC2F', '7EuHT', 'sP%mwXhGSv&'],
    # ['19', '0', '21'], ['22', '0', '101'], ['210622196908193945']
    # , ['15603858933'], ['vhozbkczkt@126.com'], ['1', '0']]
    # final_list = PairWiseUtils().pairwise(all_params, 2)
    # print(final_list)
    # print(len(final_list))
    final_list = PairWiseUtils().pairwise(get_params_from_file(sys.argv[1]), 2)
    keys = get_keys_from_file("D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\pyToSql\\keys.txt")
    data_process(final_list, keys)
    print(final_list)
