package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    /*@Autowired //생략가능(생성자가 1개일경우) 심지어 클래스에 @RequiredArgsConstructor 을 쓰면 생성자마저 생략가능.
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }*/

    @GetMapping
    public String items(Model model){ // 아이템 목록을 출력
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){
        //ModelAttribute 의 item = addAttribute에 "item"에 값을 넣어줌. 그래서 model.addAttribute생략가능해짐
        itemRepository.save(item);
        //model.addAttribute("item", item); 자동 추가로 생략 가능해짐.(@ModelAttribute)

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute("item") Item item){
        //ModelAttribute 의 item = addAttribute에 "item"에 값을 넣어줌. 그래서 model.addAttribute생략가능해짐
        itemRepository.save(item);
        //model.addAttribute("item", item); 자동 추가로 생략 가능해짐.(@ModelAttribute)

        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV4(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); //true = 상태가 저장되었다라고 생각해보자
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    //테스트용 데이터 추가. (미리 아이템 넣어놓기)
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 30));
    }
}
