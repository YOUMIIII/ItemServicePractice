package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository //@ComponentScan의 대상이 됨.
public class ItemRepository {
    private static final Map<Long, Item> store = new HashMap<>(); //static
    private static long sequence = 0L; //static spring 안에서는 싱글톤이라 안써도 되지만, 계속 new 사용해서 객체 생성하려면 static으로 사용

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values()); //store.values의 값을 그대로 가져와도 되지만 감싸주는 이유는 ArrayList에 다른 값이 들어와도 store 값에 영향을 주지 않기 위해.
    }

    public void update(Long itemId, Item updateParam){
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        //정석대로는 updateParam을 위한 dto를 따로 만드는 것이 맞다
    }

    public void clearStore(){
        store.clear(); //Map 다 날리기..
    }
}
