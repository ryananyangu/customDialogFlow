----- Menu dialog flow as tree 

[![codecov](https://codecov.io/gh/ryananyangu/customDialogFlow/branch/master/graph/badge.svg)](https://codecov.io/gh/ryananyangu/customDialogFlow)


---- Node Details:
- NodePages(int, default:1) 
- NodeHeader(txt, default:empty)
- NodeName(str, required:true)  
- NodeData (list:str->HeaderName(s),reuired:true)
- NodeCurrentPage (int, default:1)
- NodeId(int, required:true)
- NodeActiveStatus(bool, default:true)
- NodeType(enum:{IM:Input Mode,SM: Selection Mode, EC: External Call, RT: Routed From Process response(Validation)},default:IM)
- NodeExtraData(object, empty:true) // For EC must have SID

----- Graph Operations required
- Search 
- Traversals 
- Insertions 

----- Consider node input types 

1. input mode 
2. Selection mode
3. processing mode/external call (Http calls and Transformation functionality){call external system i.e. config app}
4. If the node has no children (in NodeData) then it is the final node.

----- Below will be system calculated operation(s) conducted on each node

    a) Next --- Only where pages are more than than one and there is a page to move to.
    b) Previous --- available on paginated states only and there is a previous page
    c) Exit (identifiers to be clearly defined) --- available on all Nodes and pages
    d) Home (Start screen) --- on all screens except the first one and the *exit thank you* screen
    e) Creation of paths between nodes based on **NodeData**
    f) Total pages within the nodeData
    g) input valaidation of **Input mode**

----- Implemention Considerations

    i.   WebFrameworks (SpringWeb:java,Django:python)
    ii.  Graph libraries (custom:java, JGraphT:java)
    iii. Caching of structure/Flow (Esearch,Redis)

----- Success measure points

1. Processing complexities (Mainly time and size)
2. Dynamic loading
3. No dev work (Or very much limited)



    1. if in input mode(IM) : The display test is the NodeHeader
    2. if 2 or more IM's follow each other the Only the last IM is expected to have nodeData
        a. That is greater than 1
        b. all its predecessors will have nodeData that only contains the name of their child... i.e consecutive IM's have only on child
    3. if not root node : node name can be null hence the node name will be gotten from the NOde data list of parent
