

public class A1083301_checkpoint7_RouteLinkedList {
    private A1083301_checkpoint7_Node head;
    //Description : the constructor of leading the head Node as null.
    public A1083301_checkpoint7_RouteLinkedList(){
        this.head = null;
    }
    //Description : the constructor of input a Node as the head node.
    public A1083301_checkpoint7_RouteLinkedList(A1083301_checkpoint7_Node head){
        this.head = head;
    }
    public void delete(int axis, int direction){ 
        /*********************************The TODO This Time (Checkpoint7)***************************
        //TODO(7):      Input value of Node as the reference Node,
        //              you have to delete the first Node that is same as the reference Node,
        //              and connect the following one and the previous one.
        /********************************************************************************************
         START OF YOUR CODE
        ********************************************************************************************/
        A1083301_checkpoint7_Node node = head;
        A1083301_checkpoint7_Node preNode = head;
        int preNodeCount = 0;
        while (node.getNext() != null)
        {
            if (node.getAxis() == axis && node.getDirection() == direction)
            {
                for (int i = 0; i<preNodeCount;i++)
                    preNode = preNode.getNext();
                preNode.setNext(node.getNext());
                node.setNext(null);
                break;
            }
            else
                preNodeCount++;
            node = node.getNext();
        }

        /********************************************************************************************
         END OF YOUR CODE
        ********************************************************************************************/
    }

    public A1083301_checkpoint7_Node search(int axis, int direction){
        /*******************************The TODO This Time (Checkpoint7)*****************************
        //TODO(8):      Input value of Node as the reference Node,
        //              you have to find the first Node that is same as the reference Node,
        //              and return it.
        /********************************************************************************************
         START OF YOUR CODE
        ********************************************************************************************/

        A1083301_checkpoint7_Node node = head;
        A1083301_checkpoint7_Node preNode = head;

        while (node.getNext() != null)
        {
            if (node.getAxis() == axis && node.getDirection() == direction)
                return node;
            else
                node = node.getNext();
        }
        return null;

        /********************************************************************************************
         END OF YOUR CODE
        ********************************************************************************************/
    }
    public void insert(int referenceAxis, int referenceDirection, int axis, int direction){ 
        /******************************The TODO This Time (Checkpoint7)******************************
        //TODO(9):      Input value of Node as the reference Node,
        //              and insert a Node BEFORE the first Node same as the reference Node,
        //              and connect the following one and the previous one.
        //Hint          The value of the Node is int variable axis and dirsction.
        //Hint2         If there is no reference node in linkedlist, print "Insertion null".
        /********************************************************************************************
         START OF YOUR CODE
        ********************************************************************************************/
        A1083301_checkpoint7_Node insertNode = new A1083301_checkpoint7_Node(direction, axis);
        A1083301_checkpoint7_Node referenceNode = null;
        A1083301_checkpoint7_Node node = head;
        A1083301_checkpoint7_Node preNode = head;

        if (search(referenceAxis, referenceDirection) == null)
            System.out.println("Insertion null");
        else
            referenceNode = search(referenceAxis, referenceDirection);


        int preNodeCount = 0;
        while (node.getNext() != null)
        {
            if (node.getAxis() == referenceAxis && node.getDirection() == referenceDirection)
            {
                for (int i = 0; i < preNodeCount ; i++)
                    preNode = preNode.getNext();
                break;
            }
            else
                preNodeCount++;
            node = node.getNext();
        }

        insertNode.setNext(referenceNode);
        preNode.setNext(insertNode);
        /********************************************************************************************
         END OF YOUR CODE
        ********************************************************************************************/
    }
    public int length(){
        /******************************The TODO This Time (Checkpoint7)******************************
        //TODO(10):      return how long the LinkedList is.
        /********************************************************************************************
         START OF YOUR CODE
        ********************************************************************************************/
        A1083301_checkpoint7_Node node = head;
        int count = 0;
        while (node.getNext() != null)
        {
            count++;
            node = node.getNext();
        }
        return count;
        /********************************************************************************************
         END OF YOUR CODE
        ********************************************************************************************/
    }
    public void append(int axis, int direction){
        A1083301_checkpoint7_Node current = head;
        A1083301_checkpoint7_Node newNode = new A1083301_checkpoint7_Node(direction,axis);
        if(head == null){
            head = newNode;
        }else {
            while(current.getNext() != null){
                current=current.getNext();
            }
            current.setNext(newNode);
        }
    }
    public A1083301_checkpoint7_Node getHead(){
        return this.head;
    }
    public void setHead(A1083301_checkpoint7_Node head){
        this.head = head;
    }
}
    

