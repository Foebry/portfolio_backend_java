package entities;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import annotations.Column;
import annotations.Relation;
import annotations.Serialize;

public class Serializable {
	public String serialize(Class<?> relation) {
		StringBuilder output = new StringBuilder("{");
		Field[] fields = this.getClass().getDeclaredFields();
		ArrayList<String> attributes = new ArrayList<String>();

		for (Field field : fields) {
			if (field.isAnnotationPresent(Serialize.class)) {
				Serialize annotation = field.getAnnotation(Serialize.class);
				Class<?>[] relations = annotation.relations();

				final boolean relation_missing_from_serialize_annotation = annotation.relations().length > 0
						&& !Serializable.classListContains(relations, relation);
				final boolean should_skip_field = (relation != null && relation_missing_from_serialize_annotation)
						|| annotation.serialize() == false;

				if (should_skip_field)
					continue;
				try {
					attributes.add('"' + field.getName() + '"' + ": " + this.serializeField(field));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		output.append(String.join(", ", attributes));
		output.append("}");

		return output.toString();
	}

	private String serializeField(Field field) {
		final boolean column_annotation_present = field.isAnnotationPresent(Column.class);
		final boolean relation_annotation_present = field.isAnnotationPresent(Relation.class);

		if (column_annotation_present)
			return this.serializeColumn(field);
		else if (relation_annotation_present)
			return this.serializeRelation(field);

		return null;
	}

	private String serializeColumn(Field field) {
		Column annotation = field.getAnnotation(Column.class);
		String column_type = annotation.type();
		String column_subType = annotation.subType();
		StringBuilder sb = new StringBuilder();

		try {
			switch (column_type) {
				case "Date":
					Calendar date_field = (Calendar) field.get(this);
					SimpleDateFormat sdf_date = new SimpleDateFormat("YYYY-MM-dd");
					return sb.append('"' + sdf_date.format(date_field.getTime()) + '"').toString();

				case "DateTime":
					Calendar datetime_field = (Calendar) field.get(this);
					SimpleDateFormat sdf_datetime = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
					return sb.append('"' + sdf_datetime.format(datetime_field.getTime()) + '"').toString();

				case "ArrayList":
					sb.append("[");
					ArrayList<String> items = new ArrayList<String>();
					if (column_subType.equals("Serializable")) {
						@SuppressWarnings("unchecked")
						ArrayList<Serializable> list_items = (ArrayList<Serializable>) field.get(this);
						for (Serializable list_item : list_items) {
							items.add(list_item.serialize((Class<?>) null));
						}
					}
					sb.append(String.join(", ", items));
					sb.append("]");
					return sb.toString();

				case "number":
					return sb.append(field.get(this)).toString();

				default:
					return sb.append('"' + field.get(this).toString() + '"').toString();
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String serializeRelation(Field field) {
		try {
			Company value = (Company) field.get(this);
			return value.serialize(this.getClass());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String serialize(Serializable object) throws IllegalArgumentException, IllegalAccessException {
		return object.serialize((Class<?>) null);
	}

	public static String serialize(ArrayList<Serializable> array)
			throws IllegalArgumentException, IllegalAccessException {
		StringBuilder output = new StringBuilder("[");
		ArrayList<String> objects = new ArrayList<String>();
		for (Serializable item : array) {
			String serialized_item = item.serialize((Class<?>) null);
			objects.add(serialized_item);
		}
		output.append(String.join(", ", objects));
		output.append("]");

		return output.toString();
	}

	public static boolean classListContains(Class<?>[] array, Class<?> item) {
		for (Object list_item : array) {
			if (list_item.equals(item))
				return true;
		}
		return false;
	}
};
