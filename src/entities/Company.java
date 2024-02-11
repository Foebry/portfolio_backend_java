package entities;

import annotations.Entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Random;

import annotations.Column;
import annotations.Serialize;

@Entity(name = "Company")
public class Company extends Serializable {

    @Column(type = "id")
    @Serialize()
    protected String id;

    @Column(type = "varchar")
    @Serialize(relations = { Experience.class })
    protected String name;

    @Column(type = "varchar")
    @Serialize(relations = { Company.class })
    protected String address;

    public Company(String name, String address) {
        super();

        Random random = new Random();
        final Float floatingPoint = random.nextFloat();

        this.id = Float.toString(floatingPoint).getBytes().toString();
        this.name = name;
        this.address = address;
    }

    public Company(String id, String name, String address) {
        super();

        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    static Field getRelation(Class<?> relation) {
        final Field[] fields = Entity.class.getFields();

        for (Field field : fields) {
            final Annotation annotation = field.getAnnotation(Column.class);
            if (annotation == null)
                continue;

        }
        return null;
    }

}
