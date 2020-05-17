from bs4 import BeautifulSoup
import pymysql
import time
import sys


def insert(api_data, project_name):
    # 创建数据库链接
    db = pymysql.connect(host='127.0.0.1', user='root', passwd='123456', db='autotest')
    # 创建游标:游标用于传递python给mysql的命令和mysql返回的内容
    cursor = db.cursor()
    cursor.execute(
        "INSERT INTO api(base_url,project_name,api_group,api_name,api_path,req_method,api_description,req_headers,req_query,req_body,api_response,user_id,created_at,updated_at) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
        , (api_data['base_url'], project_name, api_data['api_group'], api_data['api_name'], api_data['api_path'],
           api_data['req_method'], api_data['api_description'], api_data['req_headers'], api_data['req_query'],
           api_data['req_body'], api_data['api_response'], api_data['user_id'], api_data['created_at'],
           api_data['updated_at']))
    db.commit()
    print('插入数据成功')
    db.close()


def html_to_mysql(user_id, base_url):
    url = 'D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadApi\\api.html'
    # url = "file:///D:/Workspace/IDEA/AutoTest/src/main/resources/static/uploadApi/api.html"
    api_file = open(url, encoding='UTF-8')
    # soup = BeautifulSoup(api_file.read(), 'html5lib')
    soup = BeautifulSoup(api_file.read(), 'html.parser')
    # print(soup.prettify())
    h2_counts = 0  # h2数量
    h3_counts = 0  # h3数量
    p1_counts = 0  # p基本信息数量
    p2_counts = 0  # p请求参数数量
    p3_counts = 0  # p返回数据数量

    # project_name = soup.find_all('h1')[0]
    project_name = soup.find("h1", attrs={"class": "curproject-name"}).contents[0]  # 获取所属业务project_name
    print('所属业务project_name:')
    print(project_name.strip())  # 去前后空格

    api_groups = project_name.find_all_next("h1")  # 获取所属分组api_group

    # api_groups1 = project_name.find_all_next("h1")[1]
    # print(api_groups1)

    # api_groups1 = api_groups[0]
    # print(api_groups1)

    print("api_groups数组长度：")
    print(len(api_groups))
    for api_count in range(len(api_groups)):
        api_data = {
            "base_url": "",
            "api_group": "",
            "api_name": "",
            "api_path": "",
            "req_method": "",
            "api_description": "",
            "req_headers": "",
            "req_query": "",
            "req_body": "",
            "api_response": "",
            "user_id": "",
            "created_at": "",
            "updated_at": ""
        }
        print(api_data)
        api_group = api_groups[api_count]
        print("api_groups遍历：")
        print("所属分组api_group:")
        print(api_group.text)
        api_data["api_group"] = api_group.text
        print("处理h2之间的数据：")
        for h1_content in api_group.next_siblings:
            # print(h1_content)
            # print(h1_content.name)
            if h1_content.name == "h1":
                break
            elif h1_content.name == "h2":
                h2_counts += 1
                api_name = h1_content.text
                print("拿到h2的接口名称了！它是：" + api_name)
                # print(api_name.split("\n")[0])#处理最后的换行符
                api_data["api_name"] = api_name.split("\n")[0]
                for h2_content in h1_content.next_siblings:
                    if h2_content.name == "h2":
                        break
                    elif h2_content.name == "h3":
                        h3_counts += 1
                        # print("拿到h3了" + str(h3_counts))
                        print("拿到h3了！它是：" + h2_content.text)
                        if h2_content.text == "基本信息":
                            p1_counts += 1
                            print("基本信息的p1数量有：" + str(p1_counts))
                            for h3_content1 in h2_content.next_siblings:
                                # print(h3_content1)
                                if h3_content1.name == "h3":
                                    break
                                elif h3_content1.name == "p":
                                    # print(h3_content1.text)
                                    # print(h3_content1)
                                    # print(api_data)
                                    try:
                                        h3_p1_split = str(h3_content1).split("</strong>")
                                        h3_p1_title = h3_p1_split[0][11:-1]
                                        h3_p1_value = h3_p1_split[1][:-4].strip()
                                        # print(h3_p_split[0])
                                        # print(h3_p_split[0][11:-1])
                                        # print(h3_p_split[1][:-4].strip())
                                        if h3_p1_title == "Path":
                                            api_data["api_path"] = h3_p1_value
                                        elif h3_p1_title == "Method":
                                            api_data["req_method"] = h3_p1_value
                                        elif h3_p1_title == "接口描述":
                                            h3_p1_title_content = h3_content1.next_sibling.next_sibling
                                            if h3_p1_title_content.name == "p":
                                                h3_p1_title_content_pre = h3_p1_title_content.next_sibling.next_sibling
                                                # print(h3_p1_title_content_pre.text)
                                                if h3_p1_title_content_pre.name == "pre":
                                                    # print(h3_p1_title_content_pre.text)
                                                    # 处理返回数据里面的信息
                                                    p_pres = str(h3_p1_title_content_pre.text).split("\n")
                                                    api_desc = h3_content1.next_sibling.next_sibling.text
                                                    for p_pre in p_pres:
                                                        api_desc += p_pre.strip()
                                                    api_data["api_description"] = api_desc
                                                else:
                                                    api_data["api_description"] = h3_content1.next_sibling.next_sibling.text
                                            else:
                                                api_data["api_description"] = ""
                                    except:
                                        pass
                        elif h2_content.text == "请求参数":
                            p2_counts += 1
                            print("请求参数的p2数量有：" + str(p2_counts))
                            for h3_content2 in h2_content.next_siblings:
                                if h3_content2.name == "h3":
                                    break
                                elif h3_content2.name == "p":
                                    # print(h3_content2)
                                    try:
                                        h3_p2_split = str(h3_content2).split("/strong>")
                                        h3_p2_title = h3_p2_split[0][11:-1]
                                        # h3_p2_value = h3_p2_split[1][:-4].strip()
                                        # print(h3_p2_split[0])
                                        # print(h3_p2_split[0][11:-1])
                                        # print(h3_p2_split[1][:-4].strip())
                                        if h3_p2_title == "Headers":
                                            print("Headers")
                                            headers_table = h3_content2.next_sibling.next_sibling  # 拿到Headers内的表格
                                            headers_table_tr = headers_table.tbody.findAll("tr")  # 拿到表格内每行
                                            # print(headers_table.tbody.findAll("tr"))
                                            headers_table_list = []
                                            # print("tr个数：")
                                            # print(len(headers_table_tr))
                                            # print(range(len(headers_table_tr)))
                                            for tr in range(len(headers_table_tr)):  # 拿到每行的每格
                                                # print(table_tr[tr].findAll("td"))
                                                # for td in range(len(headers_table_tr[tr].findAll("td"))):
                                                headers_table_tr_td = headers_table_tr[tr].findAll("td")
                                                headers = {
                                                    "name": "",
                                                    "value": "",
                                                    "required": "",
                                                    "example": "",
                                                    "desc": ""
                                                }
                                                headers["name"] = headers_table_tr_td[0].text
                                                headers["value"] = headers_table_tr_td[1].text
                                                if headers_table_tr_td[2].text == "是" \
                                                        or headers_table_tr_td[2].text == "必须":
                                                    headers["required"] = "1"
                                                else:
                                                    headers["required"] = "0"
                                                headers["example"] = headers_table_tr_td[3].text
                                                headers["desc"] = headers_table_tr_td[4].text
                                                # print(headers)
                                                headers_table_list.append(headers)
                                                # print(headers_table_list)
                                                api_data["req_headers"] = str(headers_table_list)
                                        elif h3_p2_title == "Query":
                                            print("Query")
                                            query_table = h3_content2.next_sibling.next_sibling  # 拿到Headers内的表格
                                            query_table_tr = query_table.tbody.findAll("tr")  # 拿到表格内每行
                                            print(query_table.tbody.findAll("tr"))
                                            query_table_list = []
                                            # print("tr个数：")
                                            # print(len(query_table_tr))
                                            # print(range(len(query_table_tr)))
                                            for tr in range(len(query_table_tr)):  # 拿到每行的每格
                                                # print(table_tr[tr].findAll("td"))
                                                query_table_tr_td = query_table_tr[tr].findAll("td")
                                                query = {
                                                    "name": "",
                                                    "required": "",
                                                    "example": "",
                                                    "desc": ""
                                                }
                                                query["name"] = query_table_tr_td[0].text
                                                if query_table_tr_td[1].text == "是" \
                                                        or query_table_tr_td[1].text == "必须":
                                                    query["required"] = "1"
                                                else:
                                                    query["required"] = "0"
                                                query["example"] = query_table_tr_td[2].text
                                                query["desc"] = query_table_tr_td[3].text
                                                # print(query)
                                                query_table_list.append(query)
                                                # print(query_table_list)
                                                api_data["req_query"] = str(query_table_list)
                                        elif h3_p2_title == "Body":
                                            body_sibling = h3_content2.next_sibling.next_sibling
                                            print(h3_content2.next_sibling.next_sibling.name)
                                            if body_sibling.name == "table":
                                                body_table = h3_content2.next_sibling.next_sibling  # 拿到Headers内的表格
                                                body_table_tr = body_table.tbody.findAll("tr")  # 拿到表格内每行
                                                print(body_table.tbody.findAll("tr"))
                                                body_table_list = []
                                                # print("tr个数：")
                                                # print(len(body_table_tr))
                                                # print(range(len(body_table_tr)))
                                                for tr in range(len(body_table_tr)):  # 拿到每行的每格
                                                    # print(table_tr[tr].findAll("td"))
                                                    body_table_tr_td = body_table_tr[tr].findAll("td")
                                                    body = {
                                                        "name": "",
                                                        "type": "",
                                                        "required": "",
                                                        "example": "",
                                                        "desc": ""
                                                    }
                                                    body["name"] = body_table_tr_td[0].text
                                                    body["type"] = body_table_tr_td[1].text
                                                    if body_table_tr_td[2].text == "是" \
                                                            or body_table_tr_td[2].text == "必须":
                                                        body["required"] = "1"
                                                    else:
                                                        body["required"] = "0"
                                                        body["example"] = body_table_tr_td[3].text
                                                        body["desc"] = body_table_tr_td[4].text
                                                    # print(body)
                                                    body_table_list.append(body)
                                                    # print(body_table_list)
                                                    api_data["req_body"] = str(body_table_list)
                                            elif body_sibling.name == "pre":
                                                body_pres = str(body_sibling.text).split("\n")  # 处理里面的信息
                                                body_pre = ""
                                                for pre in body_pres:
                                                    body_pre += pre.strip()
                                                    api_data["req_body"] = body_pre
                                    except:
                                        pass
                        elif h2_content.text == "返回数据":
                            p3_counts += 1
                            print("返回数据的p3数量有：" + str(p3_counts))
                            for h3_content3 in h2_content.next_siblings:
                                if h3_content3.name == "h2":
                                    break
                                elif h3_content3.name == "pre":
                                    responses = str(h3_content3.text).split("\n")  # 处理返回数据里面的信息
                                    api_response = ""
                                    for response in responses:
                                        api_response += response.strip()
                                    # print(str(h3_content2.text)[0:-1])#去除抠出来的最后一个换行符
                                    api_data["api_response"] = api_response
                                    print(api_data)

                                    api_data["user_id"] = user_id
                                    api_data["base_url"] = base_url
                                    current_date_time = time.strftime('%Y-%m-%d %H:%M:%S')
                                    print(current_date_time)
                                    api_data["created_at"] = current_date_time
                                    api_data["updated_at"] = current_date_time
                                    print(api_data)
                                    insert(api_data, project_name.strip())
                        else:
                            pass
        # print("i等于：" + str(api_count))
    # print("h2数量：")
    # print(h2_counts)
    # print("h3数量：")
    # print(h3_counts)
    # print("p1数量：")
    # print(p1_counts)
    # print("p2数量：")
    # print(p2_counts)
    # print("p3数量：")
    # print(p3_counts)


if __name__ == '__main__':
    # html_to_mysql(sys.argv[1], sys.argv[2])
    html_to_mysql("1", "csr.adl.io")
# html_to_mysql(sys.argv[1], sys.argv[2])
