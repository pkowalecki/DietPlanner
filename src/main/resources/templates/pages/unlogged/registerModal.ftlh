<div class="modal" id="registerModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Register</h4>
                <button id="closeModal" type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="registerForm">
                    <div data-mdb-input-init class="form-outline mb-4">
                        <label class="form-label" for="nickname">Nickname*</label>
                        <input type="text" id="nickname" name="nickname" class="form-control" required minlength="1"
                               maxlength="50"/>
                        <div class="error-message" id="nicknameError"></div>
                    </div>
                    <div class="row">
                        <div data-mdb-input-init class="form-outline mb-4 col-md-6">
                            <label class="form-label" for="name">Name</label>
                            <input type="text" id="name" name="name" class="form-control" minlength="5" maxlength="50"/>
                            <div class="error-message" id="nameError"></div>
                        </div>
                        <div data-mdb-input-init class="form-outline mb-4 col-md-6">
                            <label class="form-label" for="surname">Surname</label>
                            <input type="text" id="surname" name="surname" class="form-control" minlength="5"
                                   maxlength="50"/>
                            <div class="error-message" id="surnameError"></div>
                        </div>
                    </div>
                    <div data-mdb-input-init class="form-outline mb-4">
                        <label class="form-label" for="emailReg">Email address*</label>
                        <input type="email" id="emailReg" name="emailReg" class="form-control" required minlength="10"
                               maxlength="80"/>
                        <div class="error-message" id="emailRegError"></div>
                    </div>
                    <div data-mdb-input-init class="form-outline mb-4">
                        <label class="form-label" for="passwordReg">Password*</label>
                        <div class="hover-text">
                            <i class="bi bi-info-circle"></i>
                            <span class="tooltip-text" id="bottom">
                                <span id="minLength">Minimum 8 znaków</span><br/>
                                <span id="lowercase">Co najmniej jedna mała litera</span><br/>
                                <span id="uppercase">Co najmniej jedna wielka litera</span><br/>
                                <span id="number">Co najmniej jedna cyfra</span><br/>
                                <span id="specialChar">Co najmniej jeden znak specjalny</span><br/>
                            </span>
                        </div>
                        <input type="password" id="passwordReg" name="passwordReg" class="form-control" required
                               minlength="8" maxlength="255"/>
                        <div class="error-message" id="passwordRegError"></div>
                    </div>
                    <div data-mdb-input-init class="form-outline mb-4">
                        <label>Strength of password</label>
                        <div class="group-pass">
                            <div class="power-container">
                                <div id="power-point"></div>
                                <div id="minRequirementsDone"></div>
                            </div>
                        </div>
                    </div>
                    <div data-mdb-input-init class="form-outline mb-4">
                        <label class="form-label" for="passwordReg2">Confirm password*</label>
                        <input type="password" id="passwordReg2" name="passwordReg2" class="form-control" required
                               minlength="8" maxlength="255"/>
                        <div class="error-message" id="passwordReg2Error"></div>
                    </div>
                    <div data-mdb-input-init class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-block">Register</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
