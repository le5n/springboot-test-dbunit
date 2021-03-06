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
package com.duck8823.matcher;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.DatabaseUnitRuntimeException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * {@link IDataSet}を比較するMatcher
 * Created by maeda on 2016/02/28.
 */
public class IDataSetMatcher extends TypeSafeMatcher<IDataSet> {

	public static Matcher<IDataSet> dataSetOf(IDataSet expected) {
		return new IDataSetMatcher(expected);
	}

	public static Matcher<IDataSet> dataSetOf(String expected) throws DataSetException {
		return new IDataSetMatcher(expected);
	}

	private final IDataSet expected;
	private String message;

	protected IDataSetMatcher(IDataSet expected) {
		this.expected = expected;
	}

	protected IDataSetMatcher(String expected) throws DataSetException {
		this.expected = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(expected));
	}

	@SuppressWarnings("Duplicates")
	@Override
	protected boolean matchesSafely(IDataSet actual) {
		try {
			Assertion.assertEquals(expected, actual);
		} catch (DatabaseUnitException e) {
			throw new DatabaseUnitRuntimeException(e);
		} catch (AssertionError e){
			message = e.getMessage();
			return false;
		}
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(expected);
		if (!StringUtils.isEmpty(message)) {
			description.appendText(message);
		}
	}
}
