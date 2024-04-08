package abai.kz.jpa.demo.controllers;

import abai.kz.jpa.demo.entities.Categories;
import abai.kz.jpa.demo.entities.Comments;
import abai.kz.jpa.demo.entities.Folders;
import abai.kz.jpa.demo.entities.Tasks;
import abai.kz.jpa.demo.repositories.CategoryRep;
import abai.kz.jpa.demo.repositories.CommentRep;
import abai.kz.jpa.demo.repositories.FolderRep;
import abai.kz.jpa.demo.repositories.TaskRep;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
    FolderRep folderRep;
    TaskRep taskRep;
    CategoryRep categoryRep;
    CommentRep commentRep;

    @GetMapping(value = "/")
    public String getIndex(Model model){
        model.addAttribute("folders", folderRep.findAll());
        return "index";
    }
    @PostMapping("/addFolder")
    public String addFolder(@RequestParam(name = "folder") String folderName) {
        Folders folders = new Folders();
        folders.setName(folderName);
        folderRep.save(folders);
        return "redirect:/";
    }
    @GetMapping("/details/{id}")
    public String details(@PathVariable(name = "id") Long id,
                          Model model) {
        Folders folders = folderRep.findById(id).orElseThrow();
        model.addAttribute("folder", folders);
        List<Tasks> tasks = taskRep.findByFolder_Id(id);
        model.addAttribute("tasks", tasks);

        List<Categories> categories = categoryRep.findAll();
        model.addAttribute("categories",categories);

        List<Categories> categoriesOfFolder = folders.getCategories();
        model.addAttribute("catOfFolder", categoriesOfFolder);
        return "details";
    }
    @PostMapping("/addTask")
    public  String addTask(@RequestParam(name = "title") String title,
                           @RequestParam(name = "description") String description,
                           @RequestParam(name = "id") Long id) {
        Tasks task = new Tasks();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(Tasks.TaskStatus.toDo);
        Folders folder = folderRep.findById(id).orElseThrow();
        task.setFolder(folder);

        taskRep.save(task);
        return "redirect:/details/" + id;
    }
    @PostMapping(value = "/addCategory")
    public String addCategory(@RequestParam(name="id") Long folderId,
                              @RequestParam(name = "category") Long catId,
                              Model model) {
        Folders folders = folderRep.findById(folderId).orElseThrow();
        model.addAttribute("folder", folders);
        List<Categories> categories = null;
        if(folders!=null) {
            if(folders.getCategories()!=null) {
                categories = folders.getCategories();
            } else {
                categories = new ArrayList<>();
            }
            Categories categories1 = categoryRep.findById(catId).orElseThrow();
            if (!folders.getCategories().contains(categories1)) {
                categories.add(categories1);
                folders.setCategories(categories);
                folderRep.save(folders);
            }
        }
        return "redirect:/details/" + folderId;
    }
    @PostMapping(value="/deleteCat")
    public String deleteCat(@RequestParam(name = "catId") Long catId,
                            @RequestParam(name = "folderId") Long folderId) {
        Folders folders = folderRep.findById(folderId).orElseThrow();
        List<Categories> categories = null;
        if(folders!=null) {
            if(folders.getCategories()!=null) {
                categories = folders.getCategories();

            } else {
                categories = new ArrayList<>();
            }
            Categories categories1 = categoryRep.findById(catId).orElseThrow();
            categories.remove(categories1);
            folders.setCategories(categories);
            folderRep.save(folders);
        }
        return "redirect:/details/" + folderId;
    }

    @GetMapping(value = "/detailsTask/{id}")
    public String detailsTask(@PathVariable(name = "id") Long id,
                              Model model) {
        Tasks tasks = taskRep.findById(id).orElseThrow();
        model.addAttribute("task",tasks);
        List<Comments> comments = commentRep.findByTask(tasks);
        model.addAttribute("comments",comments);
        return "detailsTask";
    }
    @PostMapping("/addComment")
    public String addComment(@RequestParam(name = "id") Long id,
                             @RequestParam(name = "comment") String comment) {
        Tasks tasks = taskRep.findById(id).orElseThrow();
        Comments comments = new Comments();
        comments.setComment(comment);
        comments.setTask(tasks);
        commentRep.save(comments);
        return "redirect:/detailsTask/"+id;
    }
    @GetMapping(value = "/updateTask")
    public String updateTask(@RequestParam(name="id") Long id,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "desc") String desc,
                            @RequestParam(name = "status") String status){
        Tasks tasks = taskRep.findById(id).orElseThrow();
        tasks.setTitle(name);
        tasks.setDescription(desc);
        tasks.setStatus(Tasks.TaskStatus.valueOf(status));
        taskRep.save(tasks);


        return "redirect:/detailsTask/"+id;
    }
    @PostMapping(value = "/deleteTask")
    public String deleteTask(@RequestParam(name = "id") Long id) {
        Tasks tasks =taskRep.findById(id).orElseThrow();
        Folders folders = tasks.getFolder();
        List<Comments> comments = commentRep.findByTask(tasks);
        commentRep.deleteAll(comments);
        taskRep.deleteById(id);
        return "redirect:/details/"+folders.getId();
    }
    @PostMapping(value = "/deleteFolder")
    public String deleteFolder(@RequestParam(name = "id") Long id) {
        Folders folders = folderRep.findById(id).orElseThrow();
        List<Tasks> tasks = taskRep.findByFolder_Id(folders.getId());
        for(Tasks task: tasks) {
            List<Comments> comments = commentRep.findByTask(task);
            commentRep.deleteAll(comments);
        }
        taskRep.deleteAll(tasks);
        folderRep.deleteById(folders.getId());
        return "redirect:/";
    }
}


