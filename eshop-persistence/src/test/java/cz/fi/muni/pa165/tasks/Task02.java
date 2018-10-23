package cz.fi.muni.pa165.tasks;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;


@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {

    @PersistenceUnit
    private EntityManagerFactory emf;

    private Category electro = new Category();
    private Category kitchen = new Category();
    private Product flashlight = new Product();
    private Product kitchenRobot = new Product();
    private Product plate = new Product();

    @BeforeClass
    public void createTestData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        electro.setName("Electro");
        em.persist(electro);

        kitchen.setName("Kitchen");
        em.persist(kitchen);

        flashlight.setName("Flashlight");
        flashlight.addCategory(electro);
        em.persist(flashlight);

        kitchenRobot.setName("Kitchen Robot");
        kitchenRobot.addCategory(electro);
        kitchenRobot.addCategory(kitchen);
        em.persist(kitchenRobot);

        plate.setName("Plate");
        plate.addCategory(kitchen);
        em.persist(plate);

        em.getTransaction().commit();
        em.close();
    }

    private void assertContainsCategoryWithName(Set<Category> categories,
                                                String expectedCategoryName) {
        for (Category cat : categories) {
            if (cat.getName().equals(expectedCategoryName))
                return;
        }

        Assert.fail("Couldn't find category " + expectedCategoryName + " in collection " + categories);
    }

    private void assertContainsProductWithName(Set<Product> products,
                                               String expectedProductName) {

        for (Product prod : products) {
            if (prod.getName().equals(expectedProductName))
                return;
        }

        Assert.fail("Couldn't find product " + expectedProductName + " in collection " + products);
    }


}
