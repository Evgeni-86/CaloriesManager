package ru.caloriemanager.repository.inMemory;

import ru.caloriemanager.model.AbstractBaseEntity;


//@Repository
public class InMemoryBaseRepository<T extends AbstractBaseEntity> {
//    private static AtomicInteger counter = new AtomicInteger(0);
//    Map<Integer, T> map = new ConcurrentHashMap<>();
//
//    public T save(T entry) {
//        if (entry.isNew()) {
//            entry.setId(counter.incrementAndGet());
//            map.put(entry.getId(), entry);
//            return entry;
//        }
//        return map.computeIfPresent(entry.getId(), (id, oldT) -> entry);
//    }
//
//    public boolean delete(int id) {
//        return map.remove(id) != null;
//    }
//
//    public T get(int id) {
//        return map.get(id);
//    }
//
//    Collection<T> getCollection() {
//        return map.values();
//    }
}