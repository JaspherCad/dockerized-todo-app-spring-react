import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "./Security/AutoContext"
import { addTodosBySharableLink, createTodoApi, getByIdApi, getSpecificTodoBySharableLinkApi, updateSpecificTodoBySharableLinkApi, updateTodoApi } from "./api/TodoApiServices";
import { useEffect, useState } from "react";
import {Formik, Form, Field, ErrorMessage} from 'formik'


export default function TodoComponent(){
                            //get the id from useParams() then fetch API using id from param() getByIdApi()


    //QUESTION? how to get the pre-filled data?
    //render the return valies of getById() through useEffect.
    //then set the description and targetDate using response.datas
    //map the value to initialValues of Formik.

    //then on submit, use updateTodoApi() --ofcourse through handler


    //   /welcome/:username/todo-page/containers/:containerId/todos/:todoid
    const authContext = useAuth();

    const navigate = useNavigate();

    const {username, containerId, todoid, sharelink, containerTitle} = useParams();
    const [description, setDescription] = useState('')
    const [targetDate, setTargetDate] = useState('')


    useEffect(
        () => getById(), [todoid]
    )






    //THIS PART IS TO SHOW HOW TO USE FORMIK AND MOMENT!!
    //THIS PART IS TO SHOW HOW TO USE FORMIK AND MOMENT!!
    //THIS PART IS TO SHOW HOW TO USE FORMIK AND MOMENT!!
    //THIS PART IS TO SHOW HOW TO USE FORMIK AND MOMENT!!
    //THIS PART IS TO SHOW HOW TO USE FORMIK AND MOMENT!!
    //THIS PART IS TO SHOW HOW TO USE FORMIK AND MOMENT!!
    //THIS PART IS TO SHOW HOW TO USE FORMIK AND MOMENT!!





    return(
        <>
            <div className="container">
                <h1>Enter todo details</h1>

                <Formik initialValues={{description, targetDate}} 
                enableReinitialize={true}
                onSubmit={handleSubmit}
                validate={validate}>
                
                {
                    (props) => (
                        <Form>

                            <ErrorMessage
                                name="description"
                                component="div"
                                className="alert alert-warning"
                             />

                             <ErrorMessage
                                name="targetDate"
                                component="div"
                                className="alert alert-warning"
                             />






                            <fieldset className="form-group">
                                <label>Description: </label>
                                <Field type="text" className="form-control" name="description"/>
                            </fieldset>
                            <fieldset className="form-group">
                                <label>Description: </label>
                                <Field type="date" className="form-control" name="targetDate"/>
                            </fieldset>
                            <div>
                                <button type="submit" className="btn btn-success m-3"> SUBMIT </button>
                            </div>
                        </Form>
                        
                    )


                }


                </Formik>
            </div>
        </>
    )





















    function getById(){

        
        if (todoid!=-1){
            getByIdApi(authContext.loggedUser, containerId, todoid)  //returns specific todo with unique id;
                .then( (response) =>{
                console.log("GET BY todoid ITO")
                console.log(response.data)
                setDescription(response.data.description)
                setTargetDate(response.data.targetDate)
                //DO THIS: show to page that a Todo has been deleted. USE useState.
            }) 
            .catch( (error) => console.log(error))
            .finally(() => console.log('cleanup'))
        }


        if (sharelink != null && todoid != null ){
            getSpecificTodoBySharableLinkApi(sharelink, todoid)
            .then( (response) =>{
                console.log("GET SHARED BY todoid ITO")
                console.log(response.data)
                setDescription(response.data.description)
                setTargetDate(response.data.targetDate)
                //DO THIS: show to page that a Todo has been deleted. USE useState.
            }) 
            .catch( (error) => console.log(error))
            .finally(() => console.log('cleanup'))
        }

        //
        
    }














    function handleSubmit(values){ //"values" are automatically assigned by formik. idky. ask google
        console.log(values)






        const todo = {
            // username: authContext.loggedUser, //must be username because it is in Todo Spring boot
            description: values.description,
            targetDate: values.targetDate,
            done: false
        }


        if (sharelink != null && todoid != null && todoid != -1 ){
            //for share link
            console.log(todo)
            
            updateSpecificTodoBySharableLinkApi(sharelink, todoid, todo) //FIX: SHOULD BE UPDATE!!!! 
            .then( (response) =>{
                navigate(`/share/${sharelink}`) //FIX NAVIAGATION
                //DO THIS: show to page that a Todo has been deleted. USE useState.
            }) 
            .catch( (error) => console.log(error))
            .finally(() => console.log('cleanup'))

        }

        if (sharelink != null && todoid == -1 ){
            //for share link
            console.log("CREATE SHARED TODO: " + todo.description)
            addTodosBySharableLink(sharelink, todo) //FIX: SHOULD BE UPDATE!!!! 
            .then( (response) =>{
                navigate(`/share/${sharelink}`) //FIX NAVIAGATION
                //DO THIS: show to page that a Todo has been deleted. USE useState.
            }) 
            .catch( (error) => console.log(error))
            .finally(() => console.log('cleanup'))
            
            
            
        }




        if(todoid==-1 && sharelink == null){
            createTodoApi(authContext.loggedUser, containerId, todo)
                .then( (response) =>{
                    navigate(`/welcome/${authContext.loggedUser}/todo-page/containers/${containerId}/${containerTitle}/todos`)
                    //DO THIS: show to page that a Todo has been deleted. USE useState.
                }) 
            .catch( (error) => console.log(error))
            .finally(() => console.log('cleanup'))


        }else if (todoid!=-1 && sharelink == null){
            updateTodoApi(authContext.loggedUser, containerId, todoid, todo) //in API, produce title
                .then( (response) =>{
                    navigate(`/welcome/${authContext.loggedUser}/todo-page/containers/${containerId}/${containerTitle}/todos`)
                    //DO THIS: show to page that a Todo has been deleted. USE useState.
                }) 
                .catch( (error) => console.log(error))
                .finally(() => console.log('cleanup'))
        }

        



        
         
        
        
    }
    //if without formik, after clicking submit we get the value from useState.. get the (desc and date)
    //

    function validate(values){
        let errors = {
            //description: "ENTER VALID DESCRIPTION",
            //targetDate: "ENTER VALID targetDate"

        }

        if (values.description.length<5){
            errors.description = `Enter ${5-values.description.length} More Characters`
        }

        if (values.targetDate === ""){
            errors.description = "null date!"
        }

        console.log(values)
        return errors   
    }
}


