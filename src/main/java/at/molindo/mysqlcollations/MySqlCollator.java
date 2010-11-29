/**
 * Copyright 2010 Molindo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 */
package at.molindo.mysqlcollations;

import java.io.Serializable;
import java.util.Comparator;

public class MySqlCollator implements Comparator<String>, Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private final MySqlCollation _collation;

	MySqlCollator(final MySqlCollation mySqlCollation) {
		_collation = mySqlCollation;
	}

	public int compare(final String source, final String target) {
		if (source == target) {
			return 0;
		}
		int i = 0;
		while (i < source.length() && i < target.length()) {
			final byte sWeight = _collation.getWeight(source.charAt(i));
			final byte tWeight = _collation.getWeight(target.charAt(i));
			if (sWeight != tWeight) {
				return sWeight - tWeight;
			}
			i++;
		}
		// shorter is first
		return source.length() - target.length();
	}

	public boolean equals(final String source, final String target) {
		if (source == target) {
			return true;
		}
		if (source.length() != target.length()) {
			return false;
		}

		int i = 0;
		while (i < source.length() && i < target.length()) {
			final byte sWeight = _collation.getWeight(source.charAt(i));
			final byte tWeight = _collation.getWeight(target.charAt(i));
			if (sWeight != tWeight) {
				return false;
			}
			i++;
		}
		return true;
	}

	public MySqlCollationKey getCollationKey(final String source) {
		return new MySqlCollationKey(source, _collation);
	}

	/**
	 * use lowest character of same weight for each character
     */
	public String normalize(final String string) {
		return _collation.normalize(string);
	}

	@Override
	public MySqlCollator clone() {
		try {
			return (MySqlCollator) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new RuntimeException("cloning Object not supported?", e);
		}
	}

}