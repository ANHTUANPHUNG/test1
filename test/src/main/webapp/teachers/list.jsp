<%--
  Created by IntelliJ IDEA.
  User: Tuyet
  Date: 8/7/2023
  Time: 3:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.0/jquery.min.js"
            integrity="sha512-3gJwYpMe3QewGELv8k/BX9vcqhryRdzRMxVfq6ngyWXwo03GFEzjsUm8Q7RZcHPHksttq7/GFoxjCVUjkjvPdw=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"
            integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.0/jquery.min.js"
            integrity="sha512-3gJwYpMe3QewGELv8k/BX9vcqhryRdzRMxVfq6ngyWXwo03GFEzjsUm8Q7RZcHPHksttq7/GFoxjCVUjkjvPdw=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"
            integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.css"
          integrity="sha512-3pIirOrwegjM6erE5gPSwkUzO+3cTjpnV9lexlNZqvupR64iZBnOOTiiLPb9M36zpMScbmUNIcHUqKD47M719g=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.css"
          integrity="sha512-3pIirOrwegjM6erE5gPSwkUzO+3cTjpnV9lexlNZqvupR64iZBnOOTiiLPb9M36zpMScbmUNIcHUqKD47M719g=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="../style.css" rel="stylesheet"/>
</head>
<body>
<div class="container card" style="height: 100vh">
    <div class="toast-body d-none" id="message">
        ${message}
    </div>


    <!-- Modal -->
    <form method="post" id="form">
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div id="formBody" class="row">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save changes</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <h1>Teacher</h1>
    <div class="row">
        <div class="col-2">
            <button onclick="onShowPopup()" type="button" class="btn btn-primary" data-bs-toggle="modal"
                    data-bs-target="#exampleModal">
                Create
            </button>
            <%--        <a class="btn btn-primary" href="${pageContext.request.contextPath}/users?action=create"> Create</a>--%>
        </div>
        <div class="col-4">

        </div>
        <div class="col-6" style="display: flex">
            <form action="/teachers" class="row" id="search-input">
                <div class="col-10">
                    <input type="search" name="search" value="${pageable.search}" class="form-control">
                </div>
                <div class="col-12">
                    <label for="limit">Số lượng bản ghi hiển thị:</label>
                    <select id="limit" name="limit" class="form-control" onchange="changeLimit(this.value)">
                        <c:if test="${pageable.limit == 5 }">
                            <option value="5" selected>5</option>
                            <option value="10">10</option>
                            <option value="20">20</option>
                        </c:if>
                        <c:if test="${pageable.limit == 10 }">
                            <option value="5">5</option>
                            <option value="10" selected>10</option>
                            <option value="20">20</option>
                        </c:if>
                        <c:if test="${pageable.limit == 20 }">
                            <option value="5">5</option>
                            <option value="10">10</option>
                            <option value="20" selected>20</option>
                        </c:if>


                    </select>
                </div>

                <div class="col-2">
                    <button class="btn btn-primary">
                        Search
                    </button>
                </div>
            </form>

        </div>
    </div>

    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th>
                No.
            </th>
            <th>
                <a
                        <c:if test="${pageable.sortField == 'name' && pageable.sortType == 'DESC'}">
                            href="/teachers?search=${pageable.search}&sortType=ASC&sortField=name&page=${pageable.page}"
                        </c:if>

                        <c:if test="${!(pageable.sortField == 'name' && pageable.sortType == 'DESC')}">
                            href="/teachers?search=${pageable.search}&sortType=DESC&sortField=name&page=${pageable.page}"
                        </c:if>
                >
                    Name
                </a>

            </th>
            <th>
                <a
                        <c:if test="${pageable.sortField == 'dob' && pageable.sortType == 'DESC'}">
                            href="/teachers?search=${pageable.search}&sortType=ASC&sortField=dob&page=${pageable.page}"
                        </c:if>

                        <c:if test="${!(pageable.sortField == 'dob' && pageable.sortType == 'DESC')}">
                            href="/teachers?search=${pageable.search}&sortType=DESC&sortField=dob&page=${pageable.page}"
                        </c:if>
                >
                    Date of Birth

                </a>
            </th>
            <th>
                <a
                        <c:if test="${pageable.sortField == 'hobie' && pageable.sortType == 'DESC'}">
                            href="/teachers?search=${pageable.search}&sortType=ASC&sortField=hobie&page=${pageable.page}"
                        </c:if>

                        <c:if test="${!(pageable.sortField == 'hobie' && pageable.sortType == 'DESC')}">
                            href="/teachers?search=${pageable.search}&sortType=DESC&sortField=hobie&page=${pageable.page}"
                        </c:if>
                >
                    Hobie
                </a>

            </th>
            <th>
                <a
                        <c:if test="${pageable.sortField == 'gender' && pageable.sortType == 'DESC'}">
                            href="/teachers?search=${pageable.search}&sortType=ASC&sortField=gender&page=${pageable.page}"
                        </c:if>

                        <c:if test="${!(pageable.sortField == 'gender' && pageable.sortType == 'DESC')}">
                            href="/teachers?search=${pageable.search}&sortType=DESC&sortField=gender&page=${pageable.page}"
                        </c:if>
                >
                    Gender
                </a>

            </th>
            <th>
                <a
                        <c:if test="${pageable.sortField == 'category_id' && pageable.sortType == 'DESC'}">
                            href="/teachers?search=${pageable.search}&sortType=ASC&sortField=category_id&page=${pageable.page}"
                        </c:if>

                        <c:if test="${!(pageable.sortField == 'category_id' && pageable.sortType == 'DESC')}">
                            href="/teachers?search=${pageable.search}&sortType=DESC&sortField=category_id&page=${pageable.page}"
                        </c:if>
                >
                    Category
                </a>

            </th>
            <th>
                Option
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${teachers}" var="teacher" varStatus="loop">
            <tr>
                <td>
                        ${teacher.id}
                </td>
                <td>
                        ${teacher.name}
                </td>
                <td>
                        ${teacher.dob}
                </td>
                <td>
                        ${teacher.hobie}
                </td>
                <td>
                        ${teacher.gender}
                </td>

                <td>
                        ${teacher.category.name}
                </td>
                <td>
                    <button onclick="onShowPopup(${teacher.id})" type="button" class="btn btn-primary"
                            data-bs-toggle="modal" data-bs-target="#exampleModal"> Edit
                    </button>
                    <a class="btn btn-danger" href="/teachers?action=delete&id=${teacher.id}"
                       onclick="return confirm('Do you wanna delete this ${teacher.name}')">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
    <div class="d-flex justify-content-end">

        <nav aria-label="...">
            <ul class="pagination">
                <li class="page-item <c:if test="${pageable.page == 1}">disabled</c:if>">
                    <c:if test="${pageable.page > 1}">
                        <a class="page-link"
                           href="/teachers?page=${pageable.page - 1}&search=${pageable.search}&sortType=${pageable.sortType}&sortField=${pageable.sortField}&limit=${pageable.limit}">Previous</a>
                    </c:if>
                </li>

                <c:choose>
                    <c:when test="${pageable.totalPage <= 5}">
                        <!-- Trang có ít hơn hoặc bằng 5 trang -->
                        <c:forEach begin="1" end="${pageable.totalPage}" var="num">
                            <li class="page-item <c:if test="${pageable.page == num}">active</c:if>">
                                <a class="page-link"
                                   href="/teachers?page=${num}&search=${pageable.search}&sortType=${pageable.sortType}&sortField=${pageable.sortField}&limit=${pageable.limit}">${num}</a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Trang có nhiều hơn 5 trang -->
                        <c:set var="startPage" value="${pageable.page - 2}"/>
                        <c:set var="endPage" value="${pageable.page + 2}"/>

                        <c:if test="${startPage lt 1}">
                            <c:set var="startPage" value="1"/>
                            <c:set var="endPage" value="5"/>
                        </c:if>

                        <c:if test="${endPage gt pageable.totalPage}">
                            <c:set var="endPage" value="${pageable.totalPage}"/>
                            <c:set var="startPage" value="${endPage - 4}"/>
                        </c:if>

                        <!-- Lấy điều kiện -->
                        <c:if test="${pageable.page gt pageable.totalPage - 2}">
                            <c:set var="startPage" value="${pageable.totalPage - 4}"/>
                            <c:set var="endPage" value="${pageable.totalPage}"/>
                        </c:if>
                        <c:if test="${pageable.page lt 3}">
                            <c:set var="startPage" value="1"/>
                            <c:set var="endPage" value="5"/>
                        </c:if>

                        <c:forEach begin="${startPage}" end="${endPage}" var="num">
                            <li class="page-item <c:if test="${pageable.page == num}">active</c:if>">
                                <a class="page-link"
                                   href="/teachers?page=${num}&search=${pageable.search}&sortType=${pageable.sortType}&sortField=${pageable.sortField}&limit=${pageable.limit}">${num}</a>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>

                <li class="page-item <c:if test="${pageable.page == pageable.totalPage}">disabled</c:if>">
                    <c:if test="${pageable.page < pageable.totalPage}">
                        <a class="page-link"
                           href="/teachers?page=${pageable.page + 1}&search=${pageable.search}&sortType=${pageable.sortType}&sortField=${pageable.sortField}&limit=${pageable.limit}">Next</a>
                    </c:if>
                </li>
            </ul>
        </nav>
    </div>

</div>


<script src="../base.js"></script>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
        integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
        integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
        crossorigin="anonymous"></script>

<script>
    const teachers = ${teachersJSON};
    const genders = ${genderJSON};
    const categorys = ${categorysJSON};
    let teacher = {};
    let inputs = [];
    const message = document.getElementById('message');
    const btnToast = document.getElementById('liveToastBtn');
    const form = document.getElementById('form');
    const tileModal = document.getElementById("exampleModalLabel");
    window.onload = () => {
        if (message.innerHTML.trim() !== '') {
            toastr.success(message.innerHTML);
        }
    }

    function changeLimit(limit) {
        // Cập nhật giá trị của trường limit trong đối tượng pageable
        // Bạn cần chắc chắn đã định nghĩa đối tượng pageable trước khi thêm đoạn mã này
        pageable.limit = parseInt(limit);

        // Gửi form để thực hiện tìm kiếm lại với gilimiá trị limit mới
        document.getElementById('searchForm').submit();
    }

    function onShowPopup(id) {
        let action = "create";
        let title = "Create";
        if (id) {
            action = "edit";
            title = "Edit";
        }
        tileModal.innerHTML = title + " Teacher";
        form.setAttribute('action', '/teachers?action=' + action);
        teacher = teachers.find(teacher => teacher.id === id) || {}; //
        resetData();
    }

    function resetData() {
        inputs = [
            {
                label: "Name",
                name: "name",
                pattern: "^[A-Za-z ]{6,20}",
                message: "Name must have minimun is 6 charaters and maximun is 20 charaters",
                require: true,
                classDiv: 'col-6',
                value: teacher.name || ''
            },
            {
                name: 'id',
                value: teacher.id,
                type: 'hidden',
                classDiv: 'd-none'
            },
            {
                label: "DOB",
                name: "dob",
                type: "date",
                message: "Phone must is number 10 characters",
                require: true,
                value: teacher.dob || '',
                pattern: "^[0-9]{10}",
                classDiv: 'col-6'
            },
            {
                label: "Hobie",
                name: "hobie",
                pattern: "^[A-Za-z ]{6,20}",
                message: "Name must have minimun is 6 charaters and maximun is 20 charaters",
                require: true,
                classDiv: 'col-6',
                value: teacher.hobie || ''
            },
            {
                label: "Gender",
                name: "gender",
                type: "select",
                message: "Please choose gender",
                options: genders?.map(e => {
                    return {
                        name: e,
                        value: e
                    }
                }),
                require: true,
                value: teacher.gender || '',
                classDiv: 'col-6'
            },

            {
                label: "Category",
                name: "category_id",
                type: "select",
                message: "Please choose role",
                options: categorys?.map(e => {
                    return {
                        name: e.name,
                        value: e.id
                    }
                }),
                require: true,
                value: teacher.category?.id || '', // có nghĩa là  nếu user.role có giá trị thì sẽ lấy id của role còn không thì lấy ''
                classDiv: 'col-6'
            }
        ];
        const formBody = document.getElementById('formBody'); // DOM formBody theo id
        formBody.innerHTML = '';
        // loop qua inputs
        inputs.forEach((input, index) => {
            if (input.type === 'select') {
                formBody.innerHTML += formSelect(input, index);
            } else {
                // For avatar input, set the default value to the image path (props.value) if available
                const avatarValue = input.type === 'file' ? '' : input.value;
                formBody.innerHTML += formInput({...input, value: avatarValue}, index);
            }
        });

    }
</script>
<script>
    const onFocusImg = (index) => {
        const inputsForm = document.querySelectorAll('#formBody .input-custom');
        inputsForm[index].setAttribute('focused', 'true');
    };

    function onFileChange(event, index) {
        const fileInput = event.target;
        const previewImage = document.getElementById(`previewImage` + index);
        const file = fileInput.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function () {
                previewImage.src = reader.result;
            };
            reader.readAsDataURL(file);
        } else {
            previewImage.src = '';
        }
    }
</script>
</body>
</html>
