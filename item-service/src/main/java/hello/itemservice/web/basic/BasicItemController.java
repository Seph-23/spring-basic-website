package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "basic/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/item";
  }

  @GetMapping("/add")
  public String addForm(){
    return "basic/addForm";
  }

//  @PostMapping("/add")
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

//  @PostMapping("/add")
  public String addItemV2(@ModelAttribute("item") Item item, Model model){
    itemRepository.save(item);
//    model.addAttribute("item", item);       //생략해도 데이터는 잘 들어간다.
    return "basic/item";
  }

//  @PostMapping("/add")
  public String addItemV3(@ModelAttribute Item item){    //클래스명 Item -> item 이 자동으로 ModelAttribute에 들어간다.
    itemRepository.save(item);
//    model.addAttribute("item", item);       //생략해도 데이터는 잘 들어간다.
    return "basic/item";
  }

//  @PostMapping("/add")
  public String addItemV4(Item item){    //Model Attribute 를 생략해도 동작한다.
    itemRepository.save(item);
//    model.addAttribute("item", item);       //생략해도 데이터는 잘 들어간다.
    return "basic/item";        //상품 등록 후 새로고침시에 계속 상품을 등록한다.
  }

//  @PostMapping("/add")      //Post/Redirect/Get 적용.
  public String addItemV5(Item item){    //Model Attribute 를 생략해도 동작한다.
    itemRepository.save(item);
    return "redirect:/basic/items/" + item.getId();   //PRG 를 사용하면 등록후 Get 으로 바뀌어서 새로고침시 GET을 호출.
  }           //하지만 이 방식은 URL 인코딩이 되지 않는다.

  @PostMapping("/add")
  public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/basic/items/{itemId}";
  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String edit(@PathVariable long itemId, @ModelAttribute Item item) {
    itemRepository.update(itemId, item);
    return "redirect:/basic/items/{itemId}";
  }

  /**
   * 테스트용 데이터!
  */
  @PostConstruct
  public void init(){
    itemRepository.save(new Item("itemA", 10000, 10));
    itemRepository.save(new Item("itemB", 20000, 20));
  }
}