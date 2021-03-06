/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sooner.framework.jdbc.core.parser;

import com.sooner.framework.jdbc.core.toolkit.ExceptionUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.MetaObject;

/**
 * <p>
 * 抽象 SQL 解析类
 * </p>
 *
 * @author hubin
 * @since 2017-06-20
 */
public abstract class AbstractJsqlParser implements ISqlParser {

    /**
     * 日志
     */
    protected final Log logger = LogFactory.getLog(this.getClass());

    /**
     * <p>
     * 解析 SQL 方法
     * </p>
     *
     * @param metaObject 元对象
     * @param sql        SQL 语句
     * @return SQL 信息
     */

    @Override
    public SqlInfo parser(MetaObject metaObject, String sql) {
        if (this.allowProcess(metaObject)) {
            try {
                logger.debug("Original SQL: " + sql);
                // fixed github pull/295
                StringBuilder sqlStringBuilder = new StringBuilder();
                Statements statements = CCJSqlParserUtil.parseStatements(sql);
                int i = 0;
                for (Statement statement : statements.getStatements()) {
                    if (null != statement) {
                        if (i++ > 0) {
                            sqlStringBuilder.append(';');
                        }
                        sqlStringBuilder.append(this.processParser(statement).getSql());
                    }
                }
                if (sqlStringBuilder.length() > 0) {
                    SqlInfo sqlInfo = SqlInfo.newInstance();
                    sqlInfo.setSql(sqlStringBuilder.toString());
                    return sqlInfo;
                }
            } catch (JSQLParserException e) {
                throw ExceptionUtils.mpe("Failed to process, please exclude the tableName or statementId.\n Error SQL: " + sql, e);
            }
        }
        return null;
    }

    /**
     * <p>
     * 执行 SQL 解析
     * </p>
     *
     * @param statement JsqlParser Statement
     * @return
     */
    public SqlInfo processParser(Statement statement) {
        if (statement instanceof Insert) {
            this.processInsert((Insert) statement);
        } else if (statement instanceof Select) {
            this.processSelectBody(((Select) statement).getSelectBody());
        } else if (statement instanceof Update) {
            this.processUpdate((Update) statement);
        } else if (statement instanceof Delete) {
            this.processDelete((Delete) statement);
        }
        logger.debug("parser sql: " + statement.toString());
        SqlInfo sqlInfo= SqlInfo.newInstance();
        sqlInfo.setSql(statement.toString());
        return sqlInfo;
    }

    /**
     * 新增
     */
    public abstract void processInsert(Insert insert);

    /**
     * 删除
     */
    public abstract void processDelete(Delete delete);

    /**
     * 更新
     */
    public abstract void processUpdate(Update update);

    /**
     * 查询
     */
    public abstract void processSelectBody(SelectBody selectBody);

    /**
     * <p>
     * 判断是否允许执行<br>
     * 例如：逻辑删除只解析 delete , update 操作
     * </p>
     *
     * @param metaObject 元对象
     * @return true
     */
    public boolean allowProcess(MetaObject metaObject) {
        return true;
    }
}
