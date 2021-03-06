/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Shunsuke Maeda
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.duck8823.dao;

import com.duck8823.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link com.duck8823.model.Person}にアクセスするためのDAOクラス
 * Created by maeda on 2016/02/28.
 */
@Component
public class PersonDao {

	@Autowired
	private DataSource dataSource;

	public List<Person> list() throws SQLException {
		ResultSet rs = dataSource.getConnection().createStatement().executeQuery("SELECT id, name FROM person");
		LinkedList<Person> result = new LinkedList<>();
		while(rs.next()){
			result.add(new Person(rs.getLong("id"), rs.getString("name")));
		}
		return result;
	}

	public void add(Person person) throws SQLException {
		if (person.getName() != null) {
			dataSource.getConnection().createStatement().execute("INSERT INTO person (id, name) VALUES (" + person.getId() + ", '" + person.getName() + "')");
		} else {
			dataSource.getConnection().createStatement().execute("INSERT INTO person (id) VALUES (" + person.getId() + ")");
		}
	}
}
